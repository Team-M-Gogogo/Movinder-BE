package com.movinder.be.service;

import com.movinder.be.entity.Cinema;
import com.movinder.be.entity.Movie;
import com.movinder.be.entity.MovieSession;
import com.movinder.be.entity.Seat;
import com.movinder.be.exception.IdNotFoundException;
import com.movinder.be.exception.MalformedRequestException;
import com.movinder.be.exception.MovieSession.SeatOccupiedExcpetion;
import com.movinder.be.exception.ProvidedKeyAlreadyExistException;
import com.movinder.be.exception.RequestDataNotCompleteException;
import com.movinder.be.repository.CinemaRepository;
import com.movinder.be.repository.MovieRepository;
import com.movinder.be.repository.MovieSessionRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MovieService {
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final MovieSessionRepository movieSessionRepository;
    private final ForumService forumService;

    public void deleteAll() {
        cinemaRepository.deleteAll();
        movieRepository.deleteAll();
        movieSessionRepository.deleteAll();
    }

    private static final int DEFAULT_MOVIE_SEARCH_PERIOD = 3;


    public MovieService(CinemaRepository cinemaRepository,
                        MovieRepository movieRepository,
                        MovieSessionRepository movieSessionRepository,
                        ForumService forumService){
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.forumService = forumService;

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

    public List<Cinema> getCinemaByMovieId(String movieId){

        // get set of movie ids
        Set<String> cinemaIdsSet = findMovieSessionsByMovieId(movieId)
                .stream()
                .map(MovieSession::getCinemaId)
                .collect(Collectors.toSet());

        return cinemaIdsSet.stream().map(this::findCinemaById).collect(Collectors.toList());
    }

        /*
    Movie Session
     */

    public MovieSession addMovieSession(MovieSession movieSession){
        if (movieSession.getSessionId() != null){
            throw new MalformedRequestException("Create movie session request should not contain ID");
        }
        // use the default floor plan
        movieSession.setAvailableSeatings(
                findCinemaById(movieSession.getCinemaId()).getFloorPlan());
        validateMovieSessionAttributes(movieSession);

        // check if movie exist, if not will throw exception
        findMovieById(movieSession.getMovieId());

        //check cinema exist
        findCinemaById(movieSession.getCinemaId());


        MovieSession savedMovieSession = movieSessionRepository.save(movieSession);
//        savedMovieSession.getMovieId();
        updateMovieInfo(savedMovieSession);

        return savedMovieSession;
    }

    // update lastShowTime and session ID
    private void updateMovieInfo(MovieSession movieSession){

        Movie movie = movieRepository
                .findById(movieSession.getMovieId())
                .orElseThrow(() -> new IdNotFoundException("Movie"));
        if (movieSession.getDatetime().isAfter(movie.getLastShowDateTime())){
            movie.setLastShowDateTime(movieSession.getDatetime());
        }
        if (!movie.getMovieSessionIds().contains(movieSession.getSessionId())){
            movie.getMovieSessionIds().add(movieSession.getSessionId());
        }
        movieRepository.save(movie);
    }

    public MovieSession findMovieSessionById(String id){
        Utility.validateID(id);
        return movieSessionRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Movie Session"));
    }



    public List<MovieSession> getSessionsByMovieIdAndCinemaId(String movieId, String cinemaId){
        Utility.validateID(cinemaId);
        // get set of movie ids
        return findMovieSessionsByMovieId(movieId)
                .stream()
                .filter(movieSessions -> movieSessions.getCinemaId().equals(cinemaId))
                .sorted(Comparator.comparing(MovieSession::getDatetime))
                .collect(Collectors.toList());
    }

    public List<MovieSession> findMovieSessionsByMovieId(String movieId){
        Utility.validateID(movieId);
        return movieSessionRepository.findByMovieId(movieId);
    }

    public MovieSession bookSeats(String sessionId, List<Seat> seats){
        Utility.validateID(sessionId);
        MovieSession session = movieSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new IdNotFoundException("Movie Session"));

        ArrayList<ArrayList<Boolean>> avaialbleSeatings = session.getAvailableSeatings();

        seats.forEach(seat -> {
            int row = seat.getRow();
            int col = seat.getColumn();
            try {
                Boolean available = avaialbleSeatings.get(row).get(col);
                if (available) {
                    avaialbleSeatings.get(row).set(col, false);
                } else {
                    throw new SeatOccupiedExcpetion(seat);
                }
            } catch (IndexOutOfBoundsException exception){
                throw new MalformedRequestException("Requested seat not in floor plan, col: " + col + " row: " + row);
            }

        });

        session.setAvailableSeatings(avaialbleSeatings);
        return movieSessionRepository.save(session);

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
            forumService.addChatRoom(savedMovie.getMovieId());
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
                        movie.getThumbnailUrl(),
                        movie.getThumbnailUrl(),
                        movie.getTrailerUrl())
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

    private void validateMovieSessionAttributes(MovieSession movieSession) {

        boolean containsNull = Stream
                .of(movieSession.getAvailableSeatings(),
                        movieSession.getDatetime(),
                        movieSession.getCinemaId())
                .anyMatch(Objects::isNull);

        if (containsNull){
            throw new RequestDataNotCompleteException("Movie Session");
        }

    }




}
