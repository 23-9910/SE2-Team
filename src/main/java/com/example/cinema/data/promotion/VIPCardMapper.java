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

    int insertOneCard(VIPCard vipCard);

    VIPCard selectCardById(int id);

    void updateCardBalance(@Param("id") int id,@Param("balance") double balance);

    VIPCard selectCardByUserId(int userId);

    /**
     * Modified by sun on 2019/05/29
     */
    int insertOneChargeRecord(VIPChargeRecord vipChargeRecord);

    List<VIPChargeRecord> selectChargeRecordByUserId(int userId);

    VIPChargeRecord selectChargeRecordById(int id);

    /**
     * By sun on 2019/06/09
     */
    List<VIPCard> selectAllVIPCards();

    int updateVIPChargeDescription(String description);

    String selectVIPChargeDescription();
}
