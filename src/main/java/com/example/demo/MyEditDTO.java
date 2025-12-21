package com.example.demo;

public class MyEditDTO {
    private String movieName;
    private String movieReview;
    private String origMovName;

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public void setOrigMovieName(String origMovName) {
        this.origMovName = origMovName;
    }

    public String getMovieName() {
        return movieName;
    }
    public String getMovieReview() {
        return movieReview;
    }

    public String getOrigMovName() {
        return origMovName;
    }
}
