package com.movinder.be.controller.dto;

public class AddChatRequest {
    private String customerId;
    private String message;
    private String roomId;

    public AddChatRequest(String customerId, String message, String roomId){
        this.customerId = customerId;
        this.message = message;
        this.roomId = roomId;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
