package com.movinder.be;

import com.movinder.be.controller.dto.BookingRequest;
import com.movinder.be.controller.dto.RequestItem;
import com.movinder.be.entity.*;
import com.movinder.be.repository.*;
import com.movinder.be.service.BookingService;
import com.movinder.be.service.CustomerService;
import com.movinder.be.service.MovieService;
import com.movinder.be.service.Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class BookingServiceTest {

    @Mock
    FoodRepository foodRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    BookingService bookingService;
    @Mock
    CustomerService customerService;
    @Mock
    MovieService movieService;

    @Test
    public void should_return_a_list_of_food_when_get_food_given_name(){
        // given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);

        Food food2 = new Food();
        food2.setFoodName("coke zero");
        food2.setDescription("200g");
        food2.setPrice(40);

        List<Food> foodList = Arrays.asList(food, food2);
        Pageable pageable = PageRequest.of(0, 2);

        given(foodRepository.findByfoodNameIgnoringCaseContaining("coke", pageable)).willReturn(foodList);

        // when
        List<Food> fetchFood = bookingService.getFood("coke", 0, 2);

        // then
        assertThat(fetchFood, equalTo(foodList));


        verify(foodRepository).findByfoodNameIgnoringCaseContaining("coke", pageable);
    }

    @Test
    public void should_return_a_food_when_create_food_given_a_food(){
        // given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);


        given(foodRepository.save(food)).willReturn(food);

        // when
        Food savedFood = bookingService.createFood(food);

        // then
        assertThat(savedFood, equalTo(food));

        verify(foodRepository).save(food);
    }

    @Test
    public void should_return_a_food_when_by_food_by_id_given_a_food_id(){
        // given
        Food food = new Food();
        food.setFoodId("63a00a4955506136f35be595");
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);


        given(foodRepository.findById("63a00a4955506136f35be595")).willReturn(java.util.Optional.of(food));

        // when
        Food savedFood = bookingService.findFoodById("63a00a4955506136f35be595");

        // then
        assertThat(savedFood, equalTo(food));

        verify(foodRepository).findById("63a00a4955506136f35be595");
    }

    @Test
    public void should_return_a_booking_when_create_booking_given_a_booking_request_obj(){
        // given
        String movieSessionId = "63a00a4955506136f35be596";
        String customerId = "639dab4f9370b716102e1294";
        String foodId = "639dc14cb64fa559d6100d0c";

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(customerId);
        bookingRequest.setMovieSessionId(movieSessionId);
        bookingRequest.setTicketRequestItems(new ArrayList<>(Collections.singletonList(new RequestItem("adult", 1))));
        bookingRequest.setFoodRequestItems(new ArrayList<>(Collections.singletonList(new RequestItem(foodId, 1))));

        Seat seat = new Seat(0,1);
        ArrayList<Seat> seats = new ArrayList<>(Collections.singletonList(seat));

        bookingRequest.setSeatingRequests(seats);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("name");
        customer.setPassword("pass");
        customer.setGender("Male");
        customer.setStatus("available");
        customer.setSelfIntro("intro");
        customer.setAge(20);
        customer.setShowName(false);
        customer.setShowGender(true);
        customer.setShowAge(true);
        customer.setShowStatus(true);

        Food food = new Food();
        food.setFoodId(foodId);
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);
        given(foodRepository.findById(foodId)).willReturn(java.util.Optional.of(food));

        ArrayList<ArrayList<Boolean>> availableSeatings = new ArrayList<ArrayList<Boolean>>(){
            {
                add(new ArrayList<Boolean>() {
                    {
                        add(false);
                        add(true);
                    }
                });
            }
        };

        Pricing pricing = new Pricing("adult", 10);

        MovieSession movieSession = new MovieSession();
        movieSession.setSessionId(movieSessionId);
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setAvailableSeatings(availableSeatings);
        movieSession.setCinemaId("1");
        movieSession.setMovieId("2");
        movieSession.setPricing(new ArrayList<Pricing>() {
            {
                add(pricing);
            }
        });


        ArrayList<ArrayList<Boolean>> updatedSeatings = new ArrayList<ArrayList<Boolean>>() {
            {
                add(new ArrayList<Boolean>() {
                    {
                        add(false);
                        add(false);
                    }
                });
            }
        };

        MovieSession movieSession2 = new MovieSession();
        movieSession2.setDatetime(now);
        movieSession2.setAvailableSeatings(updatedSeatings);
        movieSession2.setCinemaId("1");
        movieSession2.setMovieId("2");
        movieSession2.setPricing(new ArrayList<>());

        Ticket ticket = new Ticket("adult", 10,  new Seat(0, 1));
        String ticketId = "63a00a4955506136f35be587";
        ticket.setTicketId(ticketId);

        given(ticketRepository.save(Mockito.any(Ticket.class))).willReturn(ticket);
        given(ticketRepository.findById(ticketId)).willReturn(java.util.Optional.of(ticket));

        Booking mockBooking = new Booking(customerId, movieSessionId,
                new ArrayList<>(Arrays.asList(ticketId)),
                new ArrayList<>(Arrays.asList(foodId)),
                20);

        LocalDateTime localDateTime = LocalDateTime.parse("2020-10-10T00:00:00");
        mockBooking.setBookingTime(localDateTime);
        mockBooking.setBookingId("63a00a4955506136f35be588");

        given(bookingRepository.save(mockBooking)).willReturn(mockBooking);

        given(customerService.findByCustomerId(customerId)).willReturn(customer);

        given(movieService.findMovieSessionById(movieSessionId)).willReturn(movieSession);

        given(movieService.bookSeats(movieSessionId, bookingRequest.getSeatingRequests())).willReturn(movieSession);
        // when
        Booking booking = bookingService.createBooking(bookingRequest);


        verify(foodRepository).findById(foodId);
        verify(ticketRepository).findById(ticketId);
        verify(bookingRepository).save(mockBooking);
        verify(customerService).findByCustomerId(customerId);
        verify(movieService).findMovieSessionById(movieSessionId);
        verify(movieService).bookSeats(movieSessionId, bookingRequest.getSeatingRequests());


        // then
        assertThat(booking.getTotal(), equalTo(20));
        assertThat(booking.getBookingId(), equalTo("63a00a4955506136f35be588"));
        assertThat(booking.getCustomerId(), equalTo(customerId));
        assertThat(booking.getFoodIds(), equalTo(new ArrayList<>(Arrays.asList(foodId))));
        assertThat(booking.getTicketIds(), equalTo(new ArrayList<>(Arrays.asList(ticketId))));
        assertThat(booking.getMovieSessionId(), equalTo(movieSessionId));
        assertThat(booking.getBookingTime(), equalTo(localDateTime));

    }

    @Test
    public void should_return_bookings_when_by_get_bookings_given_a_customer_id(){
        // given
        String customerId = "639dab4f9370b716102e1294";
        String foodId = "639dc14cb64fa559d6100d0c";
        String ticketId = "639dc14cb64fa559d6100d0d";
        String movieSessionId = "63a00a4955506136f35be596";

        String from = "2022-11-10T12:00:00";
        String to = "2022-11-10T23:50:50";
        LocalDateTime fromDate = LocalDateTime.parse(from);
        LocalDateTime toDate = LocalDateTime.parse(to);

        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC, "bookingTime");

        Booking mockBooking = new Booking(customerId, movieSessionId,
                new ArrayList<>(Arrays.asList(ticketId)),
                new ArrayList<>(Arrays.asList(foodId)),
                20);


        given(bookingRepository.findByCustomerIdAndBookingTimeBetween(customerId, fromDate, toDate, pageable)).willReturn(Arrays.asList(mockBooking));

        // when
        List<Booking> bookingList = bookingService.getBookingList(customerId, 0, 1, from, to, true);

        // then
        assertThat(Arrays.asList(mockBooking), equalTo(bookingList));

        verify(bookingRepository).findByCustomerIdAndBookingTimeBetween(customerId, fromDate, toDate, pageable);
    }


}
