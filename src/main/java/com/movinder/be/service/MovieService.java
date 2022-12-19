package com.movinder.be.service;

import com.movinder.be.entity.Movie;
import com.movinder.be.exception.MalformedRequestException;
import com.movinder.be.exception.RequestDataNotCompleteException;
import com.movinder.be.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    private static final int DEFAULT_MOVIE_SEARCH_PERIOD = 3;


    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    /*
    Movie
     */

    public Movie addMovie(Movie movie){
        if (movie.getMovieId() != null){
            throw new MalformedRequestException("Create movie session request should not contain ID");
        }
        movie.setMovieSessionIds(new ArrayList<>());
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));
        validateMovieAttributes(movie);

        //todo throw duplicated key
        Movie savedMovie = movieRepository.save(movie);

        // auto add Room per new movie
//        forumService.addChatRoom(savedMovie.getMovieId());
        return savedMovie;

    }

    /*
    Checking
     */
    // checks if object contains null attributes

    private void validateMovieAttributes(Movie movie){
        boolean containsNull = Stream
                .of(movie.getMovieName(),
                        movie.getDescription(),
                        movie.getDuration(),
                        movie.getLastShowDateTime(),
                        movie.getThumbnailUrl())
                .anyMatch(Objects::isNull);

        if (containsNull){
            throw new RequestDataNotCompleteException("Movie");
        }

    }

}
