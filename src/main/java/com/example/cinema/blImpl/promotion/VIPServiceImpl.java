package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.user.AccountMapper;
import com.example.cinema.po.ConsumingRecord;
import com.example.cinema.po.User;
import com.example.cinema.po.VIPChargeRecord;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.po.VIPCard;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPConsumingSum;
import com.example.cinema.vo.VIPInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liying on 2019/4/14.
 */
@Service
public class VIPServiceImpl implements VIPService {
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public ResponseVO addVIPCard(int userId) {
        VIPCard vipCard = new VIPCard();
        vipCard.setUserId(userId);
        vipCard.setBalance(0);
        try {
            int id = vipCardMapper.insertOneCard(vipCard);
            return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getCardById(int id) {
        try {
            return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getVIPInfo() {
        VIPInfoVO vipInfoVO = new VIPInfoVO();
        vipInfoVO.setDescription(vipCardMapper.selectVIPChargeDescription());
        vipInfoVO.setPrice(VIPCard.price);
        return ResponseVO.buildSuccess(vipInfoVO);
    }

    /**
     * Modified by sun on 2019/05/28
     */
    @Override
    public ResponseVO charge(VIPCardForm vipCardForm) {
        int VIPId = vipCardForm.getVipId();
        VIPCard vipCard = vipCardMapper.selectCardById(VIPId);
        if (vipCard == null) {
            return ResponseVO.buildFailure("会员卡不存在");
        }
        double chargeAmount = vipCardForm.getAmount();
        double offerAmount = calculateOffer(chargeAmount);
        vipCard.setBalance(vipCard.getBalance() + chargeAmount + offerAmount);
        try {
            vipCardMapper.updateCardBalance(vipCardForm.getVipId(), vipCard.getBalance());
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getCardByUserId(int userId) {
        try {
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            if(vipCard==null){
                return ResponseVO.buildFailure("用户卡不存在");
            }
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * Created by sun on 2019/05/29
     */
    @Override
    public ResponseVO addChargeRecord(VIPCardForm vipCardForm){
        try{
            int VIPId = vipCardForm.getVipId();
            VIPCard vipCard = vipCardMapper.selectCardById(VIPId);
            if (vipCard == null) {
                return ResponseVO.buildFailure("会员卡不存在");
            }
            int userId = vipCard.getUserId();

            double chargeAmount = vipCardForm.getAmount();
            double offerAmount = calculateOffer(chargeAmount);
            VIPChargeRecord vipChargeRecord = new VIPChargeRecord();
            vipChargeRecord.setUserId(userId);
            vipChargeRecord.setVipId(VIPId);
            vipChargeRecord.setChargeAmount(chargeAmount);
            vipChargeRecord.setOfferAmount(offerAmount);
            vipCardMapper.insertOneChargeRecord(vipChargeRecord);
            return ResponseVO.buildSuccess();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getChargeRecordByUserId(int userId){
        try{
            List<VIPChargeRecord> vipChargeRecordList = vipCardMapper.selectChargeRecordByUserId(userId);
            return ResponseVO.buildSuccess(vipChargeRecordList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getChargeRecordById(int id) {
        try{
            VIPChargeRecord vipChargeRecord = vipCardMapper.selectChargeRecordById(id);
            return ResponseVO.buildSuccess(vipChargeRecord);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getVIPByAmount(double amount) {
        try {
            List<VIPConsumingSum> vipConsumingSumList = new ArrayList<>();
            List<VIPCard> vipCards = vipCardMapper.selectAllVIPCards();
            int len = vipCards.size();
            for(int i = 0;i < len;i++){
                VIPCard vipCard = vipCards.get(i);
                int userId = vipCard.getUserId();
                User user = accountMapper.getManagerById(userId);
                String username = user.getUsername();
                int vipId = vipCard.getId();
                List<ConsumingRecord> consumingRecords = ticketMapper.selectConsumingRecordByUser(userId);
                double sum = 0;
                for(ConsumingRecord consumingRecord : consumingRecords){
                    sum += consumingRecord.getPayment();
                }
                if(sum >= amount){
                    VIPConsumingSum vipConsumingSum = new VIPConsumingSum(userId,username,vipId,sum);
                    vipConsumingSumList.add(vipConsumingSum);
                }
            }
            return ResponseVO.buildSuccess(vipConsumingSumList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO changeDescription(String description) {
        try{
            vipCardMapper.updateVIPChargeDescription(description);
            return ResponseVO.buildSuccess();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getChargeDescription() {
        try{
            String description = vipCardMapper.selectVIPChargeDescription();
            return ResponseVO.buildSuccess(description);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * 计算充值赠送的金额,VIP优惠政策发生变动,本方法也发生变动
     * Modified by sun in 2019/05/28
     */
    public double calculateOffer(double amount) {
        String s = vipCardMapper.selectVIPChargeDescription();
        int a = Integer.parseInt(s.substring(s.indexOf('满') + 1, s.indexOf('送')));
        int b = Integer.parseInt(s.substring(s.indexOf('送') + 1));
        return (int)(amount/a)*b;
    }


}
