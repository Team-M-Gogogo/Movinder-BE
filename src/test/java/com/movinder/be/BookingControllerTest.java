package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.movinder.be.controller.dto.BookingRequest;
import com.movinder.be.controller.dto.RequestItem;
import com.movinder.be.entity.*;
import com.movinder.be.repository.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    FoodRepository foodRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    CinemaRepository cinemaRepository;


    @BeforeEach
    public void clearDB() {
        foodRepository.deleteAll();
        ticketRepository.deleteAll();
        foodRepository.deleteAll();
        movieSessionRepository.deleteAll();;
        customerRepository.deleteAll();
        movieRepository.deleteAll();
        bookingRepository.deleteAll();
        cinemaRepository.deleteAll();
    }

    @Test
    public void should_get_food_when_perform_get_food_given_params() throws Exception {
        //given

        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);

        Food food2 = new Food();
        food2.setFoodName("popcorn");
        food2.setDescription("200g");
        food2.setPrice(40);

        foodRepository.save(food);
        foodRepository.save(food2);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/booking/foods"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].foodName", containsInAnyOrder("coke", "popcorn")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].price", containsInAnyOrder(10, 40)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description", containsInAnyOrder("1L", "200g")));
    }

    @Test
    public void should_create_food_when_perform_create_given_food_obj() throws Exception {
        //given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);


        String foodJson = new ObjectMapper().writeValueAsString(food);


        //when & then
        client.perform(MockMvcRequestBuilders.post("/booking/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodName").value("coke"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("1L"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

    @Test
    public void should_return_a_food_when_get_food_by_id_given_a_food_id() throws Exception {
        // given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);

        // when
        Food savedFood = foodRepository.save(food);

        // then
        client.perform(MockMvcRequestBuilders.get("/booking/foods/{foodId}", food.getFoodId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodName").value("coke"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("1L"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

    @Test
    public void should_return_a_ticket_when_get_ticket_by_id_given_a_ticket_id() throws Exception {
        // given
        Ticket ticket = new Ticket("adult", 10, new Seat(0,1));

        // when
        Ticket savedTicket = ticketRepository.save(ticket);

        // then
        client.perform(MockMvcRequestBuilders.get("/booking/tickets/{ticketID}", ticket.getTicketId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketType").value("adult"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seat.row").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seat.column").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

    @Test
    public void should_create_booking_when_perform_booking_given_booking_request_obj() throws Exception {
        //given
        /*
        Movie
         */
        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setMovieSessionIds(new ArrayList<>());
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));

        Movie savedMovie = movieRepository.save(movie);

        /*
        Customer
         */
        Customer customer = new Customer();
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
        Customer savedCustomer = customerRepository.save(customer);

        /*
        Cinema
         */
        Cinema cinema = new Cinema();
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());
        Cinema savedCinema = cinemaRepository.save(cinema);


        /*
        Movie Session
         */
        Pricing pricing = new Pricing("adult", 10);
        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setCinemaId(savedCinema.getCinemaId());
        movieSession.setMovieId(savedMovie.getMovieId());
        movieSession.setPricing(new ArrayList<Pricing>(){
            {add(pricing);}
        });

        ArrayList<ArrayList<Boolean>> seatings = new ArrayList<ArrayList<Boolean>>() {
            {
                add(new ArrayList<Boolean>() {
                    {
                        add(false);
                        add(true);
                    }
                });
            }
        };
        movieSession.setAvailableSeatings(seatings);
        MovieSession savedMoviesSession = movieSessionRepository.save(movieSession);


        /*
        Food
         */

        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);
        Food savedFood = foodRepository.save(food);

        /*
        Booking request
         */

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(savedCustomer.getCustomerId());
        bookingRequest.setMovieSessionId(savedMoviesSession.getSessionId());
        bookingRequest.setTicketRequestItems(new ArrayList<>(Collections.singletonList(new RequestItem("adult", 1))));
        bookingRequest.setFoodRequestItems(new ArrayList<>(Collections.singletonList(new RequestItem(savedFood.getFoodId(), 1))));
        bookingRequest.setSeatingRequests(new ArrayList<>(Collections.singletonList(new Seat(0,1))));



        String bookingRequestJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(bookingRequest);



        //when & then
        client.perform(MockMvcRequestBuilders.post("/booking/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookingRequestJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookingId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(savedCustomer.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieSessionId").value(savedMoviesSession.getSessionId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketIds", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodIds", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookingTime", instanceOf(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(20));
    }



    @Test
    public void should_get_booking_when_perform_get_orders_given_customerID() throws Exception {
        //given

        Booking booking = new Booking("63a00a4955506136f35be595", "2", new ArrayList<>(Collections.singletonList("3")), new ArrayList<>(Collections.singletonList("4")), 10);
        LocalDateTime time = LocalDateTime.now();
        booking.setBookingTime(time);


        Booking savedBooking = bookingRepository.save(booking);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/booking/orders/{customerID}", "63a00a4955506136f35be595"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookingId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value("63a00a4955506136f35be595"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieSessionId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookingTime").value(time.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ticketIds", Matchers.containsInAnyOrder("3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foodIds", Matchers.containsInAnyOrder("4")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].total").value(10));
    }

}

