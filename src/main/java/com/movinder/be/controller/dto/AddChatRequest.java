package com.movinder.be.controller.dto;

public class AddChatRequest {
    private String customerId;
    private String message;
    private String movieId;

    public AddChatRequest(String customerId, String message, String movieId){
        this.customerId = customerId;
        this.message = message;
        this.movieId = movieId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
