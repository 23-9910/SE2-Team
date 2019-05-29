package com.example.cinema.po;

import java.sql.Timestamp;

/**
 * Created by sun on 2019/05/28
 */

public class VIPChargeRecord {
    /**
     * 充值记录id
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 会员卡id
     */
    private int vipId;

    /**
     * 充值金额
     */
    private double chargeAmount;

    /**
     * 赠送金额
     */
    private double offerAmount;

    /**
     * 充值时间
     */
    private Timestamp chargeTime;

    public VIPChargeRecord(){

    }

    public int getId(){return this.id;}

    public void setId(int id){this.id = id;}

    public int getUserId(){return this.userId;}

    public void setUserId(int userId){this.userId = userId;}

    public int getVipId(){return this.vipId;}

    public void setVipId(int vipId){this.vipId = vipId;}

    public double getChargeAmount(){return this.chargeAmount;}

    public void setChargeAmount(double chargeAmount){this.chargeAmount = chargeAmount;}

    public double getOfferAmount(){return this.offerAmount;}

    public void setOfferAmount(double offerAmount){this.offerAmount = offerAmount;}

    public Timestamp getChargeTime(){return this.chargeTime;}

    public void setChargeTime(Timestamp chargeTime){this.chargeTime = chargeTime;}

}
