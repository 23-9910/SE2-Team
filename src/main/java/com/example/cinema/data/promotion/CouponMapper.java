package com.example.cinema.data.promotion;

import com.example.cinema.po.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by liying on 2019/4/17.
 */
@Mapper
public interface CouponMapper {
    /**
     * 添加一张优惠券
     * @param coupon
     * @return
     */
    int insertCoupon(Coupon coupon);

    /**
     * 通过userId得到该用户的所有优惠券
     * @param userId
     * @return
     */
    List<Coupon> selectCouponByUser(int userId);

    /**
     * 通过id获得优惠券
     * @param id
     * @return
     */
    Coupon selectById(int id);

    /**
     * 给userId对应的用户添加优惠券
     * @param couponId
     * @param userId
     */
    void insertCouponUser(@Param("couponId") int couponId,@Param("userId")int userId);

    /**
     * 给userId对应的用户删除优惠券
     * @param couponId
     * @param userId
     */
    void deleteCouponUser(@Param("couponId") int couponId,@Param("userId")int userId);

    /**
     * 通过用户和消费金额挑选可用的优惠券
     * @param userId
     * @param amount
     * @return
     */
    List<Coupon> selectCouponByUserAndAmount(@Param("userId") int userId,@Param("amount") double amount);

    /**
     * 查询所有在有效期内的优惠券
     * @return
     */
    List<Coupon> selectAllCouponsCanBeUsed();
}
