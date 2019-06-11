package com.example.cinema.bl.promotion;

import com.example.cinema.po.VIPChargeRecord;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;



/**
 * Created by liying on 2019/4/14.
 */

public interface VIPService {

    ResponseVO addVIPCard(int userId);

    ResponseVO getCardById(int id);

    ResponseVO getVIPInfo();

    ResponseVO charge(VIPCardForm vipCardForm);

    ResponseVO getCardByUserId(int userId);

    /**
     * Modified bsun on 2019/05/28
     */
    ResponseVO addChargeRecord(VIPCardForm vipCardForm);

    ResponseVO getChargeRecordByUserId(int userId);

    ResponseVO getChargeRecordById(int id);

    /**
     * By sun on 2019/06/09
     */
    ResponseVO getVIPByAmount(double amount);

    //...
    ResponseVO changeDescription(String description);

    ResponseVO getChargeDescription();

}
