package com.movinder.be.controller;

import com.movinder.be.entity.Movie;
import com.movinder.be.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_PAGE_SIZE = "20";


    /*
    film
     */
    @PostMapping("/films")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Movie addMovie(@RequestBody Movie movie){
        return movieService.addMovie(movie);
    }

}
