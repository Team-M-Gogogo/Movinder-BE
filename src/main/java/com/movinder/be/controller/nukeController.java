package com.movinder.be.controller;

import com.movinder.be.controller.dto.BookingRequest;
import com.movinder.be.controller.dto.CustomerAuthenticateRequest;
import com.movinder.be.controller.dto.RequestItem;
import com.movinder.be.entity.*;
import com.movinder.be.service.BookingService;
import com.movinder.be.service.CustomerService;
import com.movinder.be.service.ForumService;
import com.movinder.be.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping("/nuke")
public class nukeController {
    private final MovieService movieService;
    private final ForumService forumService;
    private final CustomerService customerService;
    private final BookingService bookingService;

    public nukeController(MovieService movieService, ForumService forumService, CustomerService customerService, BookingService bookingService) {
        this.movieService = movieService;
        this.forumService = forumService;
        this.customerService = customerService;
        this.bookingService = bookingService;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public String deleteAll() {
        movieService.deleteAll();
        forumService.deleteAll();
        customerService.deleteAll();
        bookingService.deleteAll();

        return "Deleted all";
    }

    @GetMapping("/generate")
    @ResponseStatus(code = HttpStatus.OK)
    public String generateData() {
        Customer customer1 = createCustomer("Jack");
        Customer customer2 = createCustomer("Tom");
        
        Food food1 = bookingService.createFood(new Food("popcorn", "300ml", 55, "https://www.browneyedbaker.com/wp-content/uploads/2021/03/how-to-make-popcorn-10-square.jpg"));
        Food food2 = bookingService.createFood(new Food("Coke", "375ml", 30, "https://api.parknshop.com/medias/zoom-front-119341.jpg?context=bWFzdGVyfHBuc2hrL2ltYWdlc3wxMDUzMzN8aW1hZ2UvanBlZ3xoZDkvaGExLzk0NDE3NDI0NTQ4MTQvUE5TSEstMTE5MzQxLWZyb250LmpwZ3wwMjgzYjMxMTQ2ZjkyZDUyMDdmM2E5ZWRiZWJlNGE3NjcxZTFjMmNiMmU2OGFhZmVlZDc5NjA5MDVjZDdlNTkw"));

        System.out.println("added food");

        Cinema cinema1 = createCinema("MCL", "34 Causeway Bay Road");
        System.out.println("added cinema1");

        Cinema cinema2 = createCinema("Cinema City", "34 Java Bay Road");

        System.out.println("added cinema2");

        Movie movie1 = movieService.addMovie(new Movie("Avatar", "action movie, Sci-Fi", 140, "https://image.tmdb.org/t/p/original/6EiRUJpuoeQPghrs3YNktfnqOVh.jpg", "https://www.youtube.com/watch?v=d9MyW72ELq0&t=1s&ab_channel=Avatar"));
        Movie movie2 = movieService.addMovie(new Movie("007: No time to die", "action movie", 120, "https://m.media-amazon.com/images/M/MV5BYWQ2NzQ1NjktMzNkNS00MGY1LTgwMmMtYTllYTI5YzNmMmE0XkEyXkFqcGdeQXVyMjM4NTM5NDY@._V1_.jpg", "https://www.youtube.com/watch?v=BIhNsAtPbPI&ab_channel=JamesBond007"));


        System.out.println("added movie");
        Pricing pricing = new Pricing("Adult", 100);
        Pricing pricing2 = new Pricing("Child", 80);
        ArrayList<Pricing> pricingList = new ArrayList<>();
        pricingList.add(pricing);
        pricingList.add(pricing2);
        MovieSession cinema1session1movie1 = movieService.addMovieSession(new MovieSession(LocalDateTime.now(), cinema1.getCinemaId(), movie1.getMovieId(), pricingList));
        MovieSession cinema1session2movie1 = movieService.addMovieSession(new MovieSession(LocalDateTime.now().plusDays(1), cinema1.getCinemaId(), movie1.getMovieId(), pricingList));

        MovieSession cinema1session1movie2 = movieService.addMovieSession(new MovieSession(LocalDateTime.now(), cinema1.getCinemaId(), movie2.getMovieId(), pricingList));
        MovieSession cinema1session2movie2 = movieService.addMovieSession(new MovieSession(LocalDateTime.now().plusDays(1), cinema1.getCinemaId(), movie2.getMovieId(), pricingList));

        MovieSession cinema2session1movie1 = movieService.addMovieSession(new MovieSession(LocalDateTime.now(), cinema2.getCinemaId(), movie1.getMovieId(), pricingList));
        MovieSession cinema2session2movie1 = movieService.addMovieSession(new MovieSession(LocalDateTime.now().plusDays(1), cinema2.getCinemaId(), movie1.getMovieId(), pricingList));

        MovieSession cinema2session1movie2 = movieService.addMovieSession(new MovieSession(LocalDateTime.now(), cinema2.getCinemaId(), movie2.getMovieId(), pricingList));
        MovieSession cinema2session2movie2 = movieService.addMovieSession(new MovieSession(LocalDateTime.now().plusDays(2), cinema2.getCinemaId(), movie2.getMovieId(), pricingList));


        System.out.println("added sessions");

        RequestItem requestItem1 = new RequestItem("Adult", 2);
        RequestItem requestItem2 = new RequestItem("Child", 1);
        ArrayList<RequestItem> requestTicketItems = new ArrayList<RequestItem>( Arrays.asList(requestItem1, requestItem2));

        RequestItem foodRequestItem1 = new RequestItem(food1.getFoodId(), 1);
        RequestItem foodRequestItem2 = new RequestItem(food2.getFoodId(), 2);
        ArrayList<RequestItem> requestFoodItems =  new ArrayList<RequestItem>( Arrays.asList(foodRequestItem1, foodRequestItem2));

        Seat seat1 = new Seat(0, 1);
        Seat seat2 = new Seat(0,2);
        Seat seat3 = new Seat(0,3);
        ArrayList<Seat> seats = new ArrayList<>( Arrays.asList(seat1, seat2, seat3));


        BookingRequest br = new BookingRequest(customer1.getCustomerId(), cinema1session1movie1.getSessionId(), requestTicketItems, requestFoodItems, seats);

        bookingService.createBooking(br);

        BookingRequest br2 = new BookingRequest(customer2.getCustomerId(), cinema2session2movie2.getSessionId(), requestTicketItems, requestFoodItems, seats);

        bookingService.createBooking(br2);

        return "Done";
    }

    public Customer createCustomer(String name){
        Customer customer = new Customer();
        customer.setCustomerName(name);
        customer.setPassword("password");
        customer.setAge(20);
        customer.setGender("Male");
        customer.setStatus("Available");
        customer.setSelfIntro("intro");
        customer.setShowName(true);
        customer.setShowGender(true);
        customer.setShowStatus(false);
        customer.setShowAge(false);

        return customerService.createCustomerAccount(customer);

    }

    public Cinema createCinema(String cinemaName, String address){
        Cinema cinema = new Cinema();
        cinema.setCinemaName(cinemaName);
        cinema.setAddress(address);
        ArrayList<ArrayList<Boolean>> floorPlan = new ArrayList<>();

        for(int i=0; i<12; i++){
            ArrayList<Boolean> row = new ArrayList<>();
            for(int j=0; j<12; j++){
                row.add(Boolean.TRUE);
            }
            floorPlan.add(row);
        }

        cinema.setFloorPlan(floorPlan);
        return movieService.addCinema(cinema);

    }
}
