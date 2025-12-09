package com.example.demo;

public class MyRequestDTO {
    private String movieName;
    private String movieReview;

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public String getMovieName() {
        return movieName;
    }
    public String getMovieReview() {
        return movieReview;
    }
}
