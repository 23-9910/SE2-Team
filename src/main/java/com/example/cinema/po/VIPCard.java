
package com.example.cinema.po;


import java.sql.Timestamp;

/**
 * Created by liying on 2019/4/14.
 */

public class VIPCard {

    public static final double price = 25;

    public static String description="满200送30";

    /**
     * 用户id
     */
    private int userId;

    /**
     * 会员卡id
     */
    private int id;

    /**
     * 会员卡余额
     */
    private double balance;

    /**
     * 办卡日期
     */
    private Timestamp joinDate;


    public VIPCard() {

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    /**
     *
     * 计算充值赠送的金额,VIP优惠政策发生变动,本方法也发生变动
     * Modified by sun in 2019/05/28
     */
    public double calculateOffer(double amount) {
        String s = VIPCard.description;
        int a = Integer.parseInt(s.substring(s.indexOf('满') + 1, s.indexOf('送')));
        int b = Integer.parseInt(s.substring(s.indexOf('送') + 1));
        return (int)(amount/a)*b;

    }
    /**
     ...
     以后有时间的话，放在一张新建的数据表里
     */
}
