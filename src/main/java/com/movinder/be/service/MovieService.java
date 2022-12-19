package com.movinder.be.service;

import com.movinder.be.entity.Cinema;
import com.movinder.be.entity.Movie;
import com.movinder.be.exception.IdNotFoundException;
import com.movinder.be.exception.MalformedRequestException;
import com.movinder.be.exception.ProvidedKeyAlreadyExistException;
import com.movinder.be.exception.RequestDataNotCompleteException;
import com.movinder.be.repository.CinemaRepository;
import com.movinder.be.repository.MovieRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class MovieService {
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;

    private static final int DEFAULT_MOVIE_SEARCH_PERIOD = 3;


    public MovieService(CinemaRepository cinemaRepository, MovieRepository movieRepository){
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
    }

    /*
    Cinema
     */

    public Cinema addCinema(Cinema cinema){
        if (cinema.getCinemaId() != null){
            throw new MalformedRequestException("Create cinema request should not contain ID");
        }
        validateCinemaAttributes(cinema);
        return cinemaRepository.save(cinema);
    }

    public Cinema findCinemaById(String id){
        Utility.validateID(id);
        return cinemaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Cinema"));
    }

    public List<Cinema> getCinema(String cinemaName, Integer page, Integer pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return cinemaRepository.findBycinemaNameIgnoreCaseContaining(cinemaName, pageable);

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

        try {
            Movie savedMovie = movieRepository.save(movie);
            // auto add Room per new movie
//            forumService.addChatRoom(savedMovie.getMovieId());
            return savedMovie;
        } catch (DuplicateKeyException customerExist) {
            throw new ProvidedKeyAlreadyExistException("Movie name");
        }

    }

    public List<Movie> getMovie(String movieName, Integer page, Integer pageSize, String from, String to, boolean ascending){
        LocalDateTime fromDate = from == null ? LocalDateTime.now() : LocalDateTime.parse(from);
        LocalDateTime toDate = to == null ? LocalDateTime.now().plusMonths(DEFAULT_MOVIE_SEARCH_PERIOD) : LocalDateTime.parse(to);
        Pageable pageable = PageRequest.of(page, pageSize, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, "lastShowDateTime");

        return movieRepository
                .findBymovieNameIgnoreCaseContainingAndLastShowDateTimeBetween(movieName, fromDate, toDate, pageable);

    }

    public Movie findMovieById(String id){
        Utility.validateID(id);
        return movieRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Movie"));
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
    private void validateCinemaAttributes(Cinema cinema) {

        boolean containsNull = Stream
                .of(cinema.getAddress(),
                        cinema.getCinemaName(),
                        cinema.getFloorPlan())
                .anyMatch(Objects::isNull);

        if (containsNull){
            throw new RequestDataNotCompleteException("Cinema");
        }

    }

}
