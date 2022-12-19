package com.movinder.be.controller;

import com.movinder.be.entity.Cinema;
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
    Cinema
     */
    @PostMapping("/cinemas")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Cinema addCinema(@RequestBody Cinema cinema){
        return movieService.addCinema(cinema);
    }

    /*
    film
     */
    @PostMapping("/films")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Movie addMovie(@RequestBody Movie movie){
        return movieService.addMovie(movie);
    }

    @GetMapping("/films/{filmID}")
    @ResponseStatus(code = HttpStatus.OK)
    public Movie getMovieById(@PathVariable String filmID){
        return movieService.findMovieById(filmID);
    }

    @GetMapping("/films")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Movie> getMovie(@RequestParam(defaultValue = "") String movieName,
                                @RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                @RequestParam(required = false) String from,
                                @RequestParam(required = false) String to,
                                @RequestParam(required = false, defaultValue = "true") Boolean ascending){
        return movieService.getMovie(movieName, page, pageSize, from, to, ascending);
    }

}
