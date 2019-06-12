package com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;
import com.example.cinema.data.management.MovieMapper;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private TicketMapper ticketMapper;

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
     * Modified by sun on 2019/06/08
     * 上座率计算：该天某电影的所有售出票数量(audienceNum)/该天所有排片的影厅座位数之和(allSeats)
     */
    @Override
    public ResponseVO getMoviePlacingRateByDate(String date) {
        try{
            List<MoviePlacingRate> moviePlacingRateList = new ArrayList<>();
            List<Movie> movieList = movieMapper.selectAllMovie();

            for(int i = 0;i < movieList.size();i++){
                int audienceNum = 0;
                int allSeats = 0;
                Movie movie = movieList.get(i);
                int movieId = movie.getId();
                String movieName = movie.getName();
                List<ScheduleItem> scheduleItemList = scheduleMapper.selectScheduleByMovieId(movieId);
                for(ScheduleItem scheduleItem : scheduleItemList){
                    int scheduleId = scheduleItem.getId();
                    int hallId = scheduleItem.getHallId();
                    Hall hallOnSchedule = hallMapper.selectHallById(hallId);
                    int hallSeats = hallOnSchedule.getColumn() * hallOnSchedule.getRow();
                    String startTime = scheduleItem.getStartTime().toString().substring(0,10);
                    if(date == startTime){
                        List<Ticket> ticketList = ticketMapper.selectTicketsBySchedule(scheduleId);
                        List<Ticket> completedTickets = new ArrayList<>();
                        for(Ticket ticket1 : ticketList){
                            if(ticket1.getState()== 1){
                                completedTickets.add(ticket1);
                            }
                        }
                        audienceNum += completedTickets.size();
                        allSeats += hallSeats;
                    }else{
                        audienceNum += 0;
                        allSeats += 0;
                    }
                }
                double placingRate;
                if(allSeats == 0){
                    placingRate = 0.00;
                }else{
                    placingRate = (double) (Math.round((audienceNum/allSeats) * 100)/100);
                }
                MoviePlacingRate moviePlacingRate = new MoviePlacingRate(movieId,movieName,placingRate,date);
                moviePlacingRateList.add(moviePlacingRate);
            }
            return ResponseVO.buildSuccess(moviePlacingRateList);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * Modified by sun on 2019/06/08
     */
    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        try{
            List<PopularMovie> popularMovies = new ArrayList<>();
            List<Movie> movies = movieMapper.selectAllMovie();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, 0-days);

            for(int i = 0;i < movies.size();i++){
                Movie movie = movies.get(i);
                int movieId = movie.getId();
                String movieName = movie.getName();
                double totalBox = 0;

                List<ScheduleItem> scheduleItems = scheduleMapper.selectScheduleByMovieId(movieId);
                for(int j = 0;j < scheduleItems.size();j++){
                    ScheduleItem scheduleItem = scheduleItems.get(j);
                    Date onDate = scheduleItem.getStartTime();//放映起始日期
                    if(onDate.before(today) && onDate.after(startDate)) {
                        int scheduleId = scheduleItem.getId();
                        double fare = scheduleItem.getFare();
                        List<Ticket> completedTickets = new ArrayList<>();
                        List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
                        for(Ticket ticket2 : tickets){
                            if(ticket2.getState()== 1){
                                completedTickets.add(ticket2);
                            }
                        }
                        totalBox += completedTickets.size() * fare;
                    }
                }
                PopularMovie popularMovie = new PopularMovie(movieId,movieName,startDate,today,totalBox);
                popularMovies.add(popularMovie);
            }

            List<PopularMovie> topNumPopularMovie = new ArrayList<>();
            int len = popularMovies.size();
            for(int i = 0;i < movieNum;i++){
                for(int j = 0;j < len - 1 - i;j++){
                    PopularMovie pm1 = popularMovies.get(j);
                    PopularMovie pm2 = popularMovies.get(j+1);
                    if(pm1.getBox() > pm2.getBox()){
                        Collections.swap(popularMovies,j,j+1);
                    }
                }
                topNumPopularMovie.add(popularMovies.get(len-1));
            }
            return ResponseVO.buildSuccess(topNumPopularMovie);
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

}
