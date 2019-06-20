package com.example.cinema.blImpl.management.hall;

import com.example.cinema.bl.management.HallService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.po.Hall;
import com.example.cinema.po.ScheduleItem;
import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
@Service
public class HallServiceImpl implements HallService, HallServiceForBl {
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public ResponseVO searchAllHall() {
        try {
            return ResponseVO.buildSuccess(hallList2HallVOList(hallMapper.selectAllHall()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public Hall getHallById(int id) {
        try {
            return hallMapper.selectHallById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * By sun on 2019/05/28
     */
    @Override
    public ResponseVO addHall(HallVO hallVO){
        try {
            hallMapper.insertOneHall(hallVO);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * By sun on 2019/05/28
     */
    @Override
    public ResponseVO updateHall(HallVO hallVO){
        try {
            if( preCheck(hallVO).getSuccess()){
                return ResponseVO.buildFailure("该影厅无法修改！");
            }else {
                int hallId = hallVO.getId();
                Hall hall = hallMapper.selectHallById(hallId);
                if (hallVO.getColumn() != hall.getColumn() || hallVO.getRow() != hall.getRow()) {
                    ResponseVO responseVO = preCheck(hallVO);
                    if (!responseVO.getSuccess()) {
                        return responseVO;
                    } else {
                        hallMapper.updateHallById(hallVO);
                        return ResponseVO.buildSuccess();
                    }
                } else {
                    hallMapper.updateHallById(hallVO);
                    return ResponseVO.buildSuccess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO deleteHall(int hallId) {
        try {
            Hall hallVO =getHallById(hallId);
            if( preCheck1(hallVO).getSuccess()){
                return ResponseVO.buildFailure("该影厅无法修改！");
            }else {
                hallMapper.deleteHallById(hallId);
                return ResponseVO.buildSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    private List<HallVO> hallList2HallVOList(List<Hall> hallList){
        List<HallVO> hallVOList = new ArrayList<>();
        for(Hall hall : hallList){
            hallVOList.add(new HallVO(hall));
        }
        return hallVOList;
    }

    /**
     * 修改影厅信息的前置检查
     * 修改信息当天的后view(观众可见排片天数)天内没有排片的影厅才可以修改
     * By sun on 2019/06/08
     */
    private ResponseVO preCheck(HallVO hallVO){
        try {
            int hallId = hallVO.getId();
            int view = scheduleMapper.selectView();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date endDate = getNumDayAfterDate(today, view);
            List<ScheduleItem> scheduleItems = scheduleMapper.selectSchedule(hallId,today,endDate);
            if(scheduleItems.size() != 0){
                return ResponseVO.buildSuccess();
            }else {
                return ResponseVO.buildFailure("此影厅无法修改！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    private ResponseVO preCheck1(Hall hallVO){
        try {
            int hallId = hallVO.getId();
            int view = scheduleMapper.selectView();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date endDate = getNumDayAfterDate(today, view);
            List<ScheduleItem> scheduleItems = scheduleMapper.selectSchedule(hallId,today,endDate);
            if(scheduleItems.size() != 0){
                return ResponseVO.buildSuccess();
            }else {
                return ResponseVO.buildFailure("此影厅无法修改！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    /**
     * 获得num天后的日期
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }
}
