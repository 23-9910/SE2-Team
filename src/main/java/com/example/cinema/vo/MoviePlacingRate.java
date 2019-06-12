package com.example.cinema.vo;

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
    private String date;

    public MoviePlacingRate(int movieId, String movieName, double placingRate, String date) {
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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
