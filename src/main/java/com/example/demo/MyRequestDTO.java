package com.example.demo;

public class MyRequestDTO {
    private String movieName;
    private String movieTier;
    private String movieReview;

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setMovieTier(String movieTier) {
        this.movieTier = movieTier;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public String getMovieName() {
        return movieName;
    }
    public String getMovieTier() {
        return movieTier;
    }
    public String getMovieReview() {
        return movieReview;
    }
}
