package com.example.cinema.vo;

import java.util.*;

public class PopularMovie {
    /**
     * 电影Id
     */
    private int movieId;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 票房统计起始日期
     */
    private Date startDate;

    /**
     * 票房统计结束日期
     */
    private Date endDate;

    /**
     * 总票房
     */
    private Double box;

    public PopularMovie(int movieId, String movieName, Date startDate, Date endDate, Double box) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.box = box;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getBox() {
        return box;
    }

    public void setBox(Double box) {
        this.box = box;
    }
}
