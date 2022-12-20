package com.movinder.be.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Booking {
    @MongoId(FieldType.OBJECT_ID)
    private String bookingId;
    @Indexed
    private String customerId;

    @Indexed
    private String movieSessionId;

    private ArrayList<String> ticketIds;
    private ArrayList<String> foodIds;
    private LocalDateTime bookingTime;
    private Integer total;


    public Booking(String customerId, String movieSessionId, ArrayList<String> ticketIds, ArrayList<String> foodIds, Integer total){
        this.customerId = customerId;
        this.movieSessionId = movieSessionId;
        this.ticketIds = ticketIds;
        this.foodIds = foodIds;
        this.total = total;
        this.bookingTime = LocalDateTime.now();
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMovieSessionId() {
        return movieSessionId;
    }

    public void setMovieSessionId(String movieSessionId) {
        this.movieSessionId = movieSessionId;
    }

    public ArrayList<String> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(ArrayList<String> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public ArrayList<String> getFoodIds() {
        return foodIds;
    }

    public void setFoodIds(ArrayList<String> foodIds) {
        this.foodIds = foodIds;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Booking)) return false;
//        Booking booking = (Booking) o;
//        return Objects.equals(getBookingId(), booking.getBookingId()) && getCustomerId().equals(booking.getCustomerId()) && getMovieSessionId().equals(booking.getMovieSessionId()) && getTicketIds().equals(booking.getTicketIds()) && getFoodIds().equals(booking.getFoodIds()) && Objects.equals(getBookingTime(), booking.getBookingTime()) && getTotal().equals(booking.getTotal());
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;

        Booking booking = (Booking) o;

//        if (getBookingId() != null ? !getBookingId().equals(booking.getBookingId()) : booking.getBookingId() != null)
//            return false;
        if (!getCustomerId().equals(booking.getCustomerId())) return false;
        if (!getMovieSessionId().equals(booking.getMovieSessionId())) return false;
        if (!getTicketIds().equals(booking.getTicketIds())) return false;
        if (!getFoodIds().equals(booking.getFoodIds())) return false;
//        if (getBookingTime() != null ? !getBookingTime().equals(booking.getBookingTime()) : booking.getBookingTime() != null)
//            return false;
        if (!getTotal().equals(booking.getTotal())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookingId(), getCustomerId(), getMovieSessionId(), getTicketIds(), getFoodIds(), getBookingTime(), getTotal());
    }
}
