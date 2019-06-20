package com.example.cinema.data.promotion;

import com.example.cinema.po.VIPCard;
import com.example.cinema.po.VIPChargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liying on 2019/4/14.
 */
@Mapper
public interface VIPCardMapper {

    /**
     * 插入一张VIP卡
     * @param vipCard
     * @return
     */
    int insertOneCard(VIPCard vipCard);

    /**
     * 通过id查询VIP卡
     * @param id
     * @return
     */
    VIPCard selectCardById(int id);

    /**
     * 更新VIP卡余额
     * @param id
     * @param balance
     */
    void updateCardBalance(@Param("id") int id,@Param("balance") double balance);

    /**
     * 通过userId得到VIP卡
     * @param userId
     * @return
     */
    VIPCard selectCardByUserId(int userId);

    /**
     * Modified by sun on 2019/05/29
     * 插入一条VIP充值记录
     * @param vipChargeRecord
     * @return
     */
    int insertOneChargeRecord(VIPChargeRecord vipChargeRecord);

    /**
     * 通过userId查询历史充值记录
     * @param userId
     * @return
     */
    List<VIPChargeRecord> selectChargeRecordByUserId(int userId);

    /**
     * 通过id查找充值记录
     * @param id
     * @return
     */
    VIPChargeRecord selectChargeRecordById(int id);

    /**
     * By sun on 2019/06/09
     * 查询所有的VIP卡
     */
    List<VIPCard> selectAllVIPCards();

    /**
     * 更新VIP充值优惠策略
     * @param description
     * @return
     */
    int updateVIPChargeDescription(String description);

    /**
     * 查询VIP充值优惠策略
     * @return
     */
    String selectVIPChargeDescription();
}
