package com.example.cinema.data.promotion;

import com.example.cinema.po.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liying on 2019/4/20.
 */
@Mapper
public interface ActivityMapper {
    /**
     * 加入一个新的优惠活动
     * @param activity
     * @return
     */
    int insertActivity(Activity activity);

    /**
     * 给movieId对应的电影加上activityId的优惠活动
     * @param activityId
     * @param movieId
     * @return
     */
    int insertActivityAndMovie(@Param("activityId") int activityId,@Param("movieId") List<Integer> movieId);

    /**
     * 查询所有的优惠活动
     * @return
     */
    List<Activity> selectActivities();

    /**
     * 通过movieId得到该电影的所有优惠活动
     * @param movieId
     * @return
     */
    List<Activity> selectActivitiesByMovie(int movieId);

    /**
     * 通过id查询优惠活动
     * @param id
     * @return
     */
    Activity selectById(int id);

    /**
     * 查询所有没有分配到电影的优惠活动
     * @return
     */
    List<Activity> selectActivitiesWithoutMovie();






}
