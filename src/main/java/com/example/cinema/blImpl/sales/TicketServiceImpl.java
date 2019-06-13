package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.blImpl.promotion.ActivityServiceImpl;
import com.example.cinema.blImpl.promotion.CouponServiceImpl;
import com.example.cinema.po.VIPCard;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.vo.SeatForm;

import java.sql.Timestamp;
import java.util.List;
import java.util.*;

/**
 * Created by liying on 2019/4/16.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    ActivityServiceImpl activityService;
    @Autowired
    CouponServiceImpl couponService;



    /**
     * 将TicketForm转化成Ticket，加入TicketMapper中
     * @param
     * @return
     */
    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
        try{
            Ticket ticket = new Ticket();
            List<SeatForm> seatFormsList = ticketForm.getSeats();
            int n = seatFormsList.size();
            for(int i = 0;i < n;i++){
                ticket.setUserId(ticketForm.getUserId());
                ticket.setScheduleId(ticketForm.getScheduleId());
                ticket.setRowIndex(seatFormsList.get(i).getRowIndex());
                ticket.setColumnIndex(seatFormsList.get(i).getColumnIndex());
                ticket.setState(0);
                ticketMapper.insertTicket(ticket);
            }
            return ResponseVO.buildSuccess();
        }catch(Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    @Transactional
    public ResponseVO completeTicket(List<Integer> id, int couponId) {
        try {
            //支付成功
            for(int i = 0;i < id.size();i++){
                ticketMapper.updateTicketState(id.get(i),1);
            }
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            int userId = ticketMapper.selectTicketById(id.get(0)).getUserId();

            Ticket ticket = ticketMapper.selectTicketById(id.get(0));
            int scheduleId = ticket.getScheduleId();
            ScheduleItem scheduleItem = scheduleMapper.selectScheduleById(scheduleId);
            int movieId = scheduleItem.getMovieId();
            List<Activity> activityList = activityMapper.selectActivitiesByMovie(movieId);

            for(int i = 0;i < activityList.size();i++){
                ActivityVO activityVO = activityList.get(i).getVO();
                Timestamp startTime = activityVO.getStartTime();
                Timestamp endTime = activityVO.getEndTime();
                if(startTime.before(timestamp) && timestamp.before(endTime)){
                    Coupon coupon = activityVO.getCoupon();
                    couponMapper.insertCouponUser(coupon.getId(),userId);
                }
            }
            /**
             * 添加消费记录
             */
            addOneConsumingRecord(id,couponId,0);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * 重写了getOccupiedSeats方法，前端只显示‘0‘，‘1’两种状态的票的占座信息
     * Modified by sun on 2019/05/30
     */
    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRow()][hall.getColumn()];
            for(Ticket ticket : tickets) {
                if(ticket.getState() == 0 || ticket.getState() == 1) {
                    seats[ticket.getRowIndex()][ticket.getColumnIndex()] = 1;
                }
            }
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
        try {
            /**
             * @Author syf
             * @Info   在此类中实现了TicketWithScheduleVO的scheduleItem属性
             */
            List<Ticket> ticketList = ticketMapper.selectTicketByUser(userId);
            List<TicketWithScheduleVO> ticketWithScheduleVOList = new ArrayList<>();
            for(int i = 0;i < ticketList.size();i++){
                TicketWithScheduleVO ticketWithScheduleVO = ticketList.get(i).getWithScheduleVO();
                ScheduleItem scheduleItem = scheduleService.getScheduleItemById(ticketList.get(i).getScheduleId());
                ticketWithScheduleVO.setSchedule(scheduleItem);
                ticketWithScheduleVOList.add(ticketWithScheduleVO);
            }
            return ResponseVO.buildSuccess(ticketWithScheduleVOList);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    @Transactional
    public ResponseVO completeByVIPCard(List<Integer> id, int couponId) {
        try {
            double finalTotal = 0;
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            int userId = ticketMapper.selectTicketById(id.get(0)).getUserId();

            finalTotal = getPayment(id,couponId);

            //支付的实现
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            Double balance = vipCard.getBalance();
            if(balance < finalTotal){
                return ResponseVO.buildFailure("余额不足");
            }else{
                vipCard.setBalance(balance - finalTotal);
                vipCardMapper.updateCardBalance(vipCard.getId(),vipCard.getBalance());
                for(int i = 0;i < id.size();i++){
                    ticketMapper.updateTicketState(id.get(i),1);
                }
            }

            Ticket ticket = ticketMapper.selectTicketById(id.get(0));
            int scheduleId = ticket.getScheduleId();
            ScheduleItem scheduleItem = scheduleMapper.selectScheduleById(scheduleId);
            int movieId = scheduleItem.getMovieId();
            List<Activity> activityList = activityMapper.selectActivitiesByMovie(movieId);

            for(int i = 0;i < activityList.size();i++){
                ActivityVO activityVO = activityList.get(i).getVO();
                Timestamp startTime = activityVO.getStartTime();
                Timestamp endTime = activityVO.getEndTime();
                if(startTime.before(timestamp) && timestamp.before(endTime)){
                    Coupon coupon = activityVO.getCoupon();
                    couponMapper.insertCouponUser(coupon.getId(),userId);
                }
            }
            /**
             * 添加消费记录
             */
            addOneConsumingRecord(id,couponId,1);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO cancelTicket(List<Integer> id) {
        try {
            for(int i = 0;i < id.size();i++){
                Ticket ticket = ticketMapper.selectTicketById(id.get(i));
                ticket.setState(2);
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * 检查优惠券是否能使用
     */
    public boolean couponCheck(double firstTotal, Timestamp ticketTime, int couponId){
        try {
            Coupon coupon = new Coupon();
            coupon = couponMapper.selectById(couponId);
            double targetAmount = coupon.getTargetAmount();
            Timestamp startTime = coupon.getStartTime();
            Timestamp endTime = coupon.getEndTime();
            if(firstTotal >= targetAmount && startTime.before(ticketTime) && ticketTime.before(endTime)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseVO getTicketWithCoupon(List<Integer> ticketId,int userId){
        try{
            TicketWithCouponVO ticketWithCouponVO = new TicketWithCouponVO();
            List<TicketVO> ticketVOList = new ArrayList<>();
            List<Coupon> coupons =  new ArrayList<>();
            int movieId = 0;
            List<Coupon> allCoupons= couponMapper.selectCouponByUser(userId);
            double total = 0.0;
            Timestamp buyTime = new Timestamp(new Date().getTime());
            //获取票价，计算总价
            for(int x = 0; x < ticketId.size(); x ++){
                Ticket tmpTicket = ticketMapper.selectTicketById(ticketId.get(x));
                int tmpscheduleId = tmpTicket.getScheduleId();
                ScheduleItem tmpschedule = scheduleService.getScheduleItemById(tmpscheduleId);
                total = total + tmpschedule.getFare();
                buyTime = tmpTicket.getTime();
                ticketVOList.add(tmpTicket.getVO());
                movieId = tmpschedule.getMovieId();
            }
            //获取符合条件的优惠券
            for(int x = 0;x < allCoupons.size();x++){
                if(allCoupons.get(x).getTargetAmount() <= total && buyTime.before(allCoupons.get(x).getEndTime())
                        && allCoupons.get(x).getStartTime().before(buyTime) ) {
                    coupons.add(allCoupons.get(x));
                }
            }
            List<Activity> activities = activityMapper.selectActivitiesByMovie(movieId);
            ticketWithCouponVO.setTotal(total); //加入总价
            ticketWithCouponVO.setTicketVOList(ticketVOList); // ticketVOList
            ticketWithCouponVO.setCoupons(coupons);
            ticketWithCouponVO.setActivities(activities);
            return ResponseVO.buildSuccess(ticketWithCouponVO);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * Modified by sun on 2019/05/28
     * 2nd modified by sun on 2019/06/08
     */
    private void addOneConsumingRecord(List<Integer> ticketId,int couponId, int payForm){
        double payment = getPayment(ticketId,couponId);
        ConsumingRecord consumingRecord = new ConsumingRecord();
        consumingRecord.setCouponId(couponId);
        consumingRecord.setPayForm(payForm);
        consumingRecord.setTicketAmount(ticketId.size());
        consumingRecord.setPayment(payment);

        Ticket ticket = ticketMapper.selectTicketById(ticketId.get(0));
        int userId = ticket.getUserId();
        consumingRecord.setUserId(userId);
        int scheduleId = ticket.getScheduleId();
        consumingRecord.setScheduleId(scheduleId);

        ticketMapper.insertOneConsumingRecord(consumingRecord);
    }

    @Override
    public ResponseVO getConsumingRecordByUserId(int userId){
        try {
            List<ConsumingRecord> consumingRecordList = ticketMapper.selectConsumingRecordByUser(userId);
            return ResponseVO.buildSuccess(consumingRecordList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getConsumingRecordById(int id) {
        try {
            ConsumingRecord consumingRecord = ticketMapper.selectConsumingRecordById(id);
            return ResponseVO.buildSuccess(consumingRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * TODO:退票
     * Modified by sun on 2019/06/08
     */
    @Override
    public ResponseVO refundTicketToVIPCard(int ticketId) {
        try {
            Ticket ticket = ticketMapper.selectTicketById(ticketId);
            int userId = ticket.getUserId();
            int scheduleId = ticket.getScheduleId();
            ScheduleItem scheduleItem = scheduleMapper.selectScheduleById(scheduleId);
            double fare = scheduleItem.getFare();
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            double balance = vipCard.getBalance();
            double strategy = ticketMapper.selectRefundStrategy()/100;
            vipCardMapper.updateCardBalance(vipCard.getId(),balance + fare * strategy);
            ticketMapper.updateTicketState(ticketId,3);
            return ResponseVO.buildSuccess("退票成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO refundTicket(int ticketId){
        try {
            ticketMapper.updateTicketState(ticketId,3);
            return ResponseVO.buildSuccess("退票成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTicketByConsumingRecord(int consumingRecordId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsByConsumingRecord(consumingRecordId);
            return ResponseVO.buildSuccess(tickets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO addRecordIdOnTicket(List<Integer> ticketId) {
        try {
            Ticket ticket = ticketMapper.selectTicketById(ticketId.get(0));
            int userId = ticket.getUserId();
            List<ConsumingRecord> records = ticketMapper.selectConsumingRecordByUser(userId);
            int recordId = records.get(records.size() - 1).getId();
            for (Integer id : ticketId) {
                ticketMapper.updateRecordId(recordId, id);
            }
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    
     /** 。。。
     * @param ticketId
     * @return
     */
    @Override
    public ResponseVO returnTickets(List<Integer> ticketId) {
        try {
            List<Ticket> ticket1=new ArrayList<>();
            List<TicketWithScheduleVO> ticket2 = new ArrayList<>();
            //电影开始前两小时可以退票
            for(int i = 0;i < ticketId.size();i++){
                Ticket ticket = ticketMapper.selectTicketById(ticketId.get(i));
                if (ticket.getState()==1) {
                    ScheduleItem scheduleItem=scheduleMapper.selectScheduleById(ticket.getScheduleId());
                    //比较电影开始时间与现在时间
                    Date now = new Date();
                    long k = scheduleItem.getStartTime().getTime() - now.getTime();
                    if((k/(1000*60*60))>2){
                        ticket1.add(ticket);
                    }
                }
            }
            for(int i = 0;i < ticket1.size();i++){
                TicketWithScheduleVO ticketWithScheduleVO = ticket1.get(i).getWithScheduleVO();
                ScheduleItem scheduleItem = scheduleService.getScheduleItemById(ticket1.get(i).getScheduleId());
                ticketWithScheduleVO.setSchedule(scheduleItem);
                ticket2.add(ticketWithScheduleVO);
            }
            return ResponseVO.buildSuccess(ticket2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO changeStrategy(int strategy) {
        try {
            ticketMapper.updateRefundStrategy(strategy);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getRefundStrategy() {
        try {
            int refundPercent = ticketMapper.selectRefundStrategy();
            return ResponseVO.buildSuccess(refundPercent);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    public double getPayment(List<Integer> ticketId, int couponId){
        double firstTotal = 0;
        double payment = 0;
        int userId = 0;
        for (int i = 0; i < ticketId.size(); i++) {
            Ticket ticket = ticketMapper.selectTicketById(ticketId.get(i));
            TicketVO ticketVO = ticket.getVO();
            int scheduleId = ticketVO.getScheduleId();
            ScheduleItem scheduleItem = scheduleService.getScheduleItemById(scheduleId);
            double fare = scheduleItem.getFare();
            firstTotal += fare;
            userId = ticket.getUserId();
        }
        if (couponId != 0) {
            Coupon coupon = couponMapper.selectById(couponId);
            payment = firstTotal - coupon.getDiscountAmount();
            couponMapper.deleteCouponUser(couponId,userId);
        } else {
            payment = firstTotal;
        }
        return payment;
    }

}
