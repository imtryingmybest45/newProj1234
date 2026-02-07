package com.example.demo;

public class MyEditDTO {
    private String movieName;
    private String movieReview;
    private String origMovName;
    private String movieTier;

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public void setOrigMovieName(String origMovName) {
        this.origMovName = origMovName;
    }

    public void setMovieTier(String movieTier) {
        this.movieTier = movieTier;
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
    public String getMovieTier() {
        return movieTier;
    }
}
