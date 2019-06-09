package com.example.cinema.vo;

/**
 * By sun on 2019/06/09
 */
public class VIPConsumingSum {

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * VIP卡id
     */
    private int vipCardId;

    /**
     * 历史消费总额
     */
    private double consumingSum;

    public VIPConsumingSum(int userId, String userName, int vipCardId, double consumingSum) {
        this.userId = userId;
        this.userName = userName;
        this.vipCardId = vipCardId;
        this.consumingSum = consumingSum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVipCardId() {
        return vipCardId;
    }

    public void setVipCardId(int vipCardId) {
        this.vipCardId = vipCardId;
    }

    public double getConsumingSum() {
        return consumingSum;
    }

    public void setConsumingSum(double consumingSum) {
        this.consumingSum = consumingSum;
    }
}
