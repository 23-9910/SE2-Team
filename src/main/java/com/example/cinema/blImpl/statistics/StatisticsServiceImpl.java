package com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Team 5/6
 */
import com.example.cinema.blImpl.management.hall.HallServiceImpl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceImpl;
import com.example.cinema.blImpl.sales.TicketServiceImpl;
import com.example.cinema.data.management.ScheduleMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private HallServiceImpl hallServiceImpl;
    @Autowired
    private ScheduleServiceImpl scheduleServiceImpl;
    @Autowired
    private TicketServiceImpl ticketServiceImpl;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private HallMapper hallMapper;
    @Override
    public ResponseVO getScheduleRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));

            Date nextDate = getNumDayAfterDate(requireDate, 1);
            return ResponseVO.buildSuccess(movieScheduleTimeList2MovieScheduleTimeVOList(statisticsMapper.selectMovieScheduleTimes(requireDate, nextDate)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTotalBoxOffice() {
        try {
            return ResponseVO.buildSuccess(movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(statisticsMapper.selectMovieTotalBoxOffice()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAudiencePriceSevenDays() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -6);
            List<AudiencePriceVO> audiencePriceVOList = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                AudiencePriceVO audiencePriceVO = new AudiencePriceVO();
                Date date = getNumDayAfterDate(startDate, i);
                audiencePriceVO.setDate(date);
                List<AudiencePrice> audiencePriceList = statisticsMapper.selectAudiencePrice(date, getNumDayAfterDate(date, 1));
                double totalPrice = audiencePriceList.stream().mapToDouble(item -> item.getTotalPrice()).sum();
                audiencePriceVO.setPrice(Double.parseDouble(String.format("%.2f", audiencePriceList.size() == 0 ? 0 : totalPrice / audiencePriceList.size())));
                audiencePriceVOList.add(audiencePriceVO);
            }
            return ResponseVO.buildSuccess(audiencePriceVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * 实现了StatisticsService接口的getMoviePlacingRateByDate方法，返回ResponseVO(String)
     * @param date
     * @return
     */
    @Override
    public ResponseVO getMoviePlacingRateByDate(Date date) {
        try{
            ResponseVO responseVOHall = hallServiceImpl.searchAllHall();
            List<HallVO> listOfHallVO = (ArrayList<HallVO>)responseVOHall.getContent();
            ResponseVO responseVOOneDaySchedule = new ResponseVO();
            List<ScheduleVO> listOfOneDaySchedule= new ArrayList<ScheduleVO>();
            List<ScheduleItemVO> listOfScheduleItemVO = new ArrayList<ScheduleItemVO>();
            int n = listOfHallVO.size();
            int m = 0;
            int audienceNum = 0;
            int scheduleNum = 0;
            for(int i = 0;i < n;i++){
                HallVO singleHall = listOfHallVO.get(i);
                m += singleHall.getRow()*singleHall.getColumn();
                int id = singleHall.getId();
                responseVOOneDaySchedule = scheduleServiceImpl.searchScheduleOneDay(id,date);
                listOfOneDaySchedule = (ArrayList<ScheduleVO>)responseVOOneDaySchedule.getContent();
                ScheduleVO scheduleVO = listOfOneDaySchedule.get(0);
                listOfScheduleItemVO = scheduleVO.getScheduleItemList();
                int oneHallScheduleNum = listOfScheduleItemVO.size();
                scheduleNum += oneHallScheduleNum;
                for(int j = 0;j < oneHallScheduleNum;j++) {
                    int scheduleId = listOfScheduleItemVO.get(j).getId();
                    ResponseVO responseVOTicket = ticketServiceImpl.getBySchedule(scheduleId);
                    int[][] seat = (int[][]) responseVOTicket.getContent();
                    for (int k = 0; k < seat.length; k++) {
                        for (int s = 0; s < seat[0].length; s++) {
                            if (seat[k][s] == 1) {
                                audienceNum++;
                            }
                        }
                    }
                }
            }
            float moviePlacingRateByDate = (audienceNum/scheduleNum/m/n)*100;
            DecimalFormat decimalFormat = new DecimalFormat("0.###");
            String getMoviePlacingRateByDate = decimalFormat.format(moviePlacingRateByDate);
            return ResponseVO.buildSuccess(Float.parseFloat(getMoviePlacingRateByDate));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        try{
            List<Hall> hallList = hallMapper.selectAllHall();
            Date today = new Date();
            Date startDay = getNumDayBeforeDate(today,days);
            ArrayList<String> movieList = new ArrayList();
            ArrayList<Double> boxList = new ArrayList();
            for(int i = 0;i < hallList.size();i++){
                int hallId = hallList.get(i).getId();
                List<ScheduleItem> scheduleItemList = this.getScheduleInDaysDay(hallId,startDay,days);
                for(int j = 0;j < scheduleItemList.size();j++){
                    int scheduleId = scheduleItemList.get(j).getId();
                    double fare = scheduleItemList.get(j).getFare();
                    String movieName = scheduleItemList.get(j).getMovieName();
                    ResponseVO responseVO = ticketServiceImpl.getBySchedule(scheduleId);
                    ScheduleWithSeatVO scheduleWithSeatVO = (ScheduleWithSeatVO)responseVO.getContent();
                    int totalSeat = 0;
                    double totalBox;
                    int[][] seats = scheduleWithSeatVO.getSeats();
                    int len1 = seats.length;
                    int len2 = seats[0].length;
                    for(int m = 0;m < len1;m++){
                        for(int n = 0;n < len2;n++){
                            if (seats[m][n] == 1){
                                totalSeat++;
                            }
                        }
                    }
                    totalBox = totalSeat * fare;
                    for (int a = 0;a < movieList.size();a++){
                        if( movieList.get(a)== movieName){
                            double startBox = boxList.get(a);
                            boxList.set(a,startBox + totalBox);
                        }else{
                            movieList.add(movieName);
                            boxList.add(totalBox);
                        }
                    }
                }
            }
            for(int i = 0;i < movieList.size() - 1;i++){
                for(int j = i;j < movieList.size() - 1 - i;j++){
                    if(boxList.get(j) < boxList.get(j+1)){
                        String bigId = movieList.get(j+1);
                        String smallId = movieList.get(j);
                        movieList.set(j,bigId);
                        movieList.set(j+1,smallId);

                        double bigBox = boxList.get(j+1);
                        double smallBox = boxList.get(j);
                        boxList.set(j,bigBox);
                        boxList.set(j+1,smallBox);
                    }
                }
            }
            ArrayList<String> movieNumMovieId = new ArrayList<>();
            for(int i = 0;i < movieNum;i++){
                movieNumMovieId.add(movieList.get(i));
            }
            return ResponseVO.buildSuccess(movieNumMovieId);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }


    /**
     * 获得num天后的日期
     * @param oldDate
     * @param num
     * @return
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }

    /**
     * 获得num天前的日期
     * @param
     * @return
     */
    Date getNumDayBeforeDate(Date nowDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(nowDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, -num);
        return calendarTime.getTime();
    }



    private List<MovieScheduleTimeVO> movieScheduleTimeList2MovieScheduleTimeVOList(List<MovieScheduleTime> movieScheduleTimeList){
        List<MovieScheduleTimeVO> movieScheduleTimeVOList = new ArrayList<>();
        for(MovieScheduleTime movieScheduleTime : movieScheduleTimeList){
            movieScheduleTimeVOList.add(new MovieScheduleTimeVO(movieScheduleTime));
        }
        return movieScheduleTimeVOList;
    }


    private List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(List<MovieTotalBoxOffice> movieTotalBoxOfficeList){
        List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeVOList = new ArrayList<>();
        for(MovieTotalBoxOffice movieTotalBoxOffice : movieTotalBoxOfficeList){
            movieTotalBoxOfficeVOList.add(new MovieTotalBoxOfficeVO(movieTotalBoxOffice));
        }
        return movieTotalBoxOfficeVOList;
    }

    /**
     * 获得最近days天内所有的排片计划
     */
    private List<ScheduleItem> getScheduleInDaysDay(int hallId, Date startDate,int days){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            startDate = simpleDateFormat.parse(simpleDateFormat.format(startDate));
            Date endDate = getNumDayAfterDate(startDate, days);
            List<ScheduleItem> scheduleItemList = scheduleMapper.selectSchedule(hallId, startDate, endDate);
            return scheduleItemList;
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}
