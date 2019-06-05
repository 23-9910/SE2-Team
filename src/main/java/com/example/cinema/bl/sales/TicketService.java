package com.example.cinema.bl.sales;

import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.TicketForm;

import java.util.List;


/**
 * Created by liying on 2019/4/16.
 */
public interface TicketService {
    /**
     * TODO:锁座【增加票但状态为未付款】
     *
     * @param ticketForm
     * @return
     */
    ResponseVO addTicket(TicketForm ticketForm);

    /**
     * TODO:完成购票【不使用会员卡】流程包括校验优惠券和根据优惠活动赠送优惠券
     *
     * @param id
     * @param couponId
     * @return
     */
    ResponseVO completeTicket(List<Integer> id, int couponId);

    /**
     * 获得该场次的被锁座位和场次信息
     *
     * @param scheduleId
     * @return
     */
    ResponseVO getBySchedule(int scheduleId);

    /**
     * TODO:获得用户买过的票
     *
     * @param userId
     * @return
     */
    ResponseVO getTicketByUser(int userId);

    /**
     * TODO:完成购票【使用会员卡】流程包括会员卡扣费、校验优惠券和根据优惠活动赠送优惠券
     *
     * @param id
     * @param couponId
     * @return
     */
    ResponseVO completeByVIPCard(List<Integer> id, int couponId);

    /**
     * TODO:取消锁座（只有状态是"锁定中"的可以取消）
     *
     * @param id
     * @return
     */
    ResponseVO cancelTicket(List<Integer> id);

    /**
     * 通过userId获取用户
     * @param userId
     * @return
     */
    ResponseVO getTicketWithCoupon(List<Integer> ticketId,int userId);

    /**
     * Modified by sun on 2019/05/28
     */

    /**
     * 添加一条消费记录
     */
    ResponseVO addOneConsumingRecord(List<Integer> ticketId, int couponId, int payForm);

    /**
     * 获得用户的所有消费记录
     */
    ResponseVO getConsumingRecordByUserId(int userId);

    /**
     * 通过id获得消费记录
     */
    ResponseVO getConsumingRecordById(int id);

    /**
     * 退票
     * 退一张票，只有操作时间早于电影开始时间才能退票
     * 退票返回用户原票价的60%费用，已使用的优惠券和已发放的优惠券不再进行操作
     */
    ResponseVO refundTicket(int ticketId);

    /**
     * 通过消费记录id获得票
     */
    ResponseVO getTicketByConsumingRecord (int consumingRecordId);

    /**
     * 为买的票添加消费记录id
     */
    ResponseVO addRecordIdOnTicket(List<Integer> ticketId);
    /**
     *...
     */
    ResponseVO returnTickets(List<Integer> ticketId);
}

