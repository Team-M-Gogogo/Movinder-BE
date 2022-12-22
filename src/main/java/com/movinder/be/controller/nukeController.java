package com.movinder.be.controller;

import com.movinder.be.service.BookingService;
import com.movinder.be.service.CustomerService;
import com.movinder.be.service.ForumService;
import com.movinder.be.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

}
