package com.example.cinema.po;

import java.sql.Timestamp;

/**
 * Created by sun on 2019/05/28
 */
public class ConsumingRecord {
    /**
     * 消费记录id
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 付款金额
     */
    private double payment;

    /**
     * 付款方式
     * 0: 银行卡; 1: VIP卡
     */
    private int payForm;

    /**
     * 消费时间
     */
    private Timestamp payTime;

    /**
     * 排片id
     */
    private int scheduleId;

    /**
     * 电影票数量
     */
    private int ticketAmount;

    /**
     * 优惠券id
     * 0表示未使用优惠券
     */
    private int couponId;

    public ConsumingRecord(){

    }

    public int getId(){return this.id;}

    public void setId(int id){this.id = id;}

    public int getUserId(){return this.userId;}

    public void setUserId(int userId){this.userId = userId;}

    public double getPayment(){return this.payment;}

    public void setPayment(double payment){this.payment = payment;}

    public int getPayForm(){return this.payForm;}

    public void setPayForm(int payForm){this.payForm = payForm;}

    public Timestamp getPayTime(){return this.payTime;}

    public void setPayTime(Timestamp payTime){this.payTime = payTime;}

    public int getScheduleId() { return scheduleId; }

    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public int getTicketAmount(){return this.ticketAmount;}

    public void setTicketAmount(int ticketAmount){this.ticketAmount = ticketAmount;}

    public int getCouponId(){return this.couponId;}

    public void setCouponId(int couponId){this.couponId = couponId;}
}
