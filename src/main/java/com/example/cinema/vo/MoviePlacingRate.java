package com.example.cinema.vo;

import java.util.Date;

public class MoviePlacingRate {
    /**
     * 电影Id
     */
    private int movieId;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 此电影某天的上座率
     */
    private double placingRate;

    /**
     * 日期
     */
    private Date date;

    public MoviePlacingRate(int movieId, String movieName, double placingRate, Date date) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.placingRate = placingRate;
        this.date = date;
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

    public double getPlacingRate() {
        return placingRate;
    }

    public void setPlacingRate(double placingRate) {
        this.placingRate = placingRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
