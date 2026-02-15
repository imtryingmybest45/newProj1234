package com.example.demo;

public class MyRequestDTO {
    private String movieName;
    private String movieTier;
    private String movieReview;
    private String movieYear;

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setMovieTier(String movieTier) {
        this.movieTier = movieTier;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public void setMovieYear(String movieYear) {this.movieYear = movieYear;}

    public String getMovieName() {
        return movieName;
    }
    public String getMovieTier() {
        return movieTier;
    }
    public String getMovieReview() {
        return movieReview;
    }
    public String getMovieYear() {return movieYear;}
}
