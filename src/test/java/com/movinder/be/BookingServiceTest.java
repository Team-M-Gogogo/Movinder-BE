package com.movinder.be;

import com.movinder.be.controller.dto.BookingRequest;
import com.movinder.be.controller.dto.RequestItem;
import com.movinder.be.entity.*;
import com.movinder.be.repository.*;
import com.movinder.be.service.BookingService;
import com.movinder.be.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class BookingServiceTest {

    @Mock
    FoodRepository foodRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    MovieSessionRepository movieSessionRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    BookingService bookingService;
    @InjectMocks
    CustomerService customerService;

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

//    @Test
//    public void should_return_a_booking_when_create_booking_given_a_booking_request_obj(){
//        // given
//        String movieSessionId = "63a00a4955506136f35be596";
//        String customerId = "63a00a4955506136f35be595";
//        String foodId = "'63a00a4955506136f35be597'";
//
//        BookingRequest bookingRequest = new BookingRequest();
//        bookingRequest.setCustomerId(customerId);
//        bookingRequest.setMovieSessionId(movieSessionId);
//        bookingRequest.setTicketRequestItems(new ArrayList<>(Collections.singletonList(new RequestItem("adult", 1))));
//        bookingRequest.setFoodRequestItems(new ArrayList<>(Collections.singletonList(new RequestItem(foodId, 1))));
//        bookingRequest.setSeatingRequests(new ArrayList<>(Collections.singletonList(new Seat(0,1))));
//
//        Customer customer = new Customer();
//        customer.setCustomerId(customerId);
//        customer.setCustomerName("name");
//        customer.setPassword("pass");
//        customer.setGender("Male");
//        customer.setStatus("available");
//        customer.setSelfIntro("intro");
//        customer.setAge(20);
//        customer.setShowName(false);
//        customer.setShowGender(true);
//        customer.setShowAge(true);
//        customer.setShowStatus(true);
//        given(customerRepository.findById(customerId)).willReturn(java.util.Optional.of(customer));
//
//        Food food = new Food();
//        food.setFoodId(foodId);
//        food.setFoodName("coke");
//        food.setDescription("1L");
//        food.setPrice(10);
//        given(foodRepository.findById(foodId)).willReturn(java.util.Optional.of(food));
//
//        ArrayList<ArrayList<Boolean>> availableSeatings = new ArrayList<ArrayList<Boolean>>(){
//            {
//                add(new ArrayList<Boolean>() {
//                    {
//                        add(false);
//                        add(true);
//                    }
//                });
//            }
//        };
//
//        Pricing pricing = new Pricing("adult", 10);
//
//        MovieSession movieSession = new MovieSession();
//        String start = "1970-01-01T00:00:00";
//        LocalDateTime now = LocalDateTime.parse(start);
//        movieSession.setDatetime(now);
//        movieSession.setAvailableSeatings(availableSeatings);
//        movieSession.setCinemaId("1");
//        movieSession.setMovieId("2");
//        movieSession.setPricing(new ArrayList<Pricing>() {
//            {
//                add(pricing);
//            }
//        });
//
//        given(movieSessionRepository.findById(movieSessionId)).willReturn(java.util.Optional.of(movieSession));
//
//        ArrayList<ArrayList<Boolean>> updatedSeatings = new ArrayList<ArrayList<Boolean>>() {
//            {
//                add(new ArrayList<Boolean>() {
//                    {
//                        add(false);
//                        add(false);
//                    }
//                });
//            }
//        };
//        //todo check add movie session will update
//
//        MovieSession movieSession2 = new MovieSession();
//        movieSession2.setDatetime(now);
//        movieSession2.setAvailableSeatings(updatedSeatings);
//        movieSession2.setCinemaId("1");
//        movieSession2.setMovieId("2");
//        movieSession2.setPricing(new ArrayList<>());
//
//        given(movieSessionRepository.save(movieSession)).willReturn(movieSession2);
//
//        Ticket ticket = new Ticket("adult", 110, new Seat(0,1));
//        String ticketId = "63a00a4955506136f35be587";
//        ticket.setTicketId(ticketId);
//
//        given(ticketRepository.save(ticket)).willReturn(ticket);
//
//        given(bookingRepository.save(new Booking(customerId, movieSessionId,
//                new ArrayList<>(Arrays.asList(ticketId)),
//                new ArrayList<>(Arrays.asList(foodId)), 110)))
//                .willReturn(new Booking(customerId, movieSessionId,
//                        new ArrayList<>(Arrays.asList(ticketId)),
//                        new ArrayList<>(Arrays.asList(foodId)),
//                        110));
//
//        given(customerService.findByCustomerId(customerId)).willReturn(java.util.Optional.of(customer));
//
//
//        // when
//        Booking booking = bookingService.createBooking(bookingRequest);
//
//        // then
//        assertThat(booking, equalTo(new Booking(customerId, movieSessionId,
//                new ArrayList<>(Arrays.asList(ticketId)),
//                new ArrayList<>(Arrays.asList(foodId)),
//                110)));
//    }
}
