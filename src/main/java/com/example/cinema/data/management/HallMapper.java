package com.example.cinema.data.management;

import com.example.cinema.po.Hall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fjj
 * @date 2019/4/11 3:46 PM
 */
@Mapper
public interface HallMapper {
    /**
     * 查询所有影厅信息
     * @return
     */
    List<Hall> selectAllHall();

    /**
     * 根据id查询影厅
     * @return
     */
    Hall selectHallById(@Param("hallId") int hallId);

    /**
     * 添加影厅(影厅信息录入)
     * By sun on 2019/05/28
     */
    int insertOneHall(Hall hall);

    /**
     * 修改影厅信息
     * By sun on 2019/05/28
     */
    int updateHallById(Hall hall);
}
