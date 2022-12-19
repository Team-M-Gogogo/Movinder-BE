package com.movinder.be;

import com.movinder.be.entity.Cinema;
import com.movinder.be.entity.Movie;
import com.movinder.be.entity.MovieSession;
import com.movinder.be.repository.CinemaRepository;
import com.movinder.be.repository.MovieRepository;
import com.movinder.be.repository.MovieSessionRepository;
import com.movinder.be.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class MovieServiceTest {

    @Mock
    MovieRepository movieRepository;

    @Mock
    CinemaRepository cinemaRepository;

    @Mock
    MovieSessionRepository movieSessionRepository;

    @InjectMocks
    MovieService movieService;

    @Test
    public void should_return_a_Movie_when_add_movie_given_a_movie(){
        // given
        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");


        given(movieRepository.save(movie)).willReturn(movie);

        // when
        Movie savedMovie = movieService.addMovie(movie);

        // then
        assertThat(savedMovie.getMovieName(), equalTo("Avengers"));
        assertThat(savedMovie.getDescription(), equalTo("Action movie"));
        assertThat(savedMovie.getDuration(), equalTo(100));
        assertThat(savedMovie.getThumbnailUrl(), equalTo("http://testurl"));
        assertTrue(savedMovie.getMovieSessionIds().isEmpty());
        assertThat(savedMovie.getLastShowDateTime(),
                equalTo(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0))));


        verify(movieRepository).save(movie);
    }

    @Test
    public void should_return_a_list_of_movie_when_get_movie_given_movies(){
        // given
        Movie movie1 = new Movie();
        movie1.setMovieName("Avengers");
        movie1.setDescription("Action movie");
        movie1.setDuration(100);
        movie1.setThumbnailUrl("http://testurl");

        Movie movie2 = new Movie();
        movie2.setMovieName("Avengers 2");
        movie2.setDescription("Action movie");
        movie2.setDuration(100);
        movie2.setThumbnailUrl("http://testurl");

        List<Movie> movies = Arrays.asList(movie1, movie2);

        String start = "1970-01-01T00:00:00";
        String end = "1971-01-01T00:00:00";

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        Pageable pageable = PageRequest.of(0, 2,  Sort.Direction.DESC, "lastShowDateTime");


        given(movieRepository.findBymovieNameIgnoreCaseContainingAndLastShowDateTimeBetween("Avengers", startDate, endDate, pageable)).willReturn(movies);
        // when
        List<Movie> fetchMovies = movieService
                .getMovie("Avengers", 0, 2, start, end, false);

        // then
        assertThat(fetchMovies, equalTo(movies));


        verify(movieRepository).findBymovieNameIgnoreCaseContainingAndLastShowDateTimeBetween("Avengers", startDate, endDate, pageable);
    }

    @Test
    public void should_return_a_Movie_when_get_movie_by_id_given_a_movie_id(){
        // given
        Movie movie = new Movie();
        String movieId = "63a00a4955506136f35be595";
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setMovieSessionIds(new ArrayList<>());
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));


        given(movieRepository.findById(movieId)).willReturn(java.util.Optional.of(movie));

        // when
        Movie savedMovie = movieService.findMovieById(movieId);

        // then
        assertThat(savedMovie.getMovieName(), equalTo("Avengers"));
        assertThat(savedMovie.getDescription(), equalTo("Action movie"));
        assertThat(savedMovie.getDuration(), equalTo(100));
        assertThat(savedMovie.getThumbnailUrl(), equalTo("http://testurl"));
        assertTrue(savedMovie.getMovieSessionIds().isEmpty());
        assertThat(savedMovie.getLastShowDateTime(),
                equalTo(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0))));


        verify(movieRepository).findById(movieId);
    }

    @Test
    public void should_return_a_Cinema_when_add_cinema_given_a_cinema(){
        // given
        Cinema cinema = new Cinema();
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());

        given(cinemaRepository.save(cinema)).willReturn(cinema);

        // when
        Cinema savedCinema = movieService.addCinema(cinema);

        // then
        assertThat(savedCinema.getCinemaName(), equalTo("MCL"));
        assertThat(savedCinema.getAddress(), equalTo("address"));
        assertThat(savedCinema.getFloorPlan(), empty());

        verify(cinemaRepository).save(cinema);
    }

    @Test
    public void should_return_a_Cinema_when_get_cinema_by_id_given_a_cinema_id(){
        // given

        Cinema cinema = new Cinema();
        String cinemaId = "63a00a4955506136f35be595";
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());


        given(cinemaRepository.findById(cinemaId)).willReturn(java.util.Optional.of(cinema));

        // when
        Cinema savedCinema = movieService.findCinemaById(cinemaId);

        // then
        assertThat(savedCinema.getCinemaName(), equalTo("MCL"));
        assertThat(savedCinema.getAddress(), equalTo("address"));
        assertThat(savedCinema.getFloorPlan(), empty());

        verify(cinemaRepository).findById(cinemaId);
    }

    @Test
    public void should_return_a_list_of_cinema_when_get_cinema_given_cinemas(){
        // given
        Cinema cinema1 = new Cinema();
        cinema1.setAddress("address");
        cinema1.setCinemaName("MCL");
        cinema1.setFloorPlan(new ArrayList<>());

        Cinema cinema2 = new Cinema();
        cinema2.setAddress("address 2");
        cinema2.setCinemaName("MCL 2");
        cinema2.setFloorPlan(new ArrayList<>());

        List<Cinema> cinemas = Arrays.asList(cinema1, cinema2);

        Pageable pageable = PageRequest.of(0, 2);


        given(cinemaRepository.findBycinemaNameIgnoreCaseContaining("Avengers", pageable)).willReturn(cinemas);
        // when
        List<Cinema> fetchCinemas = movieService
                .getCinema("Avengers", 0, 2);

        // then
        assertThat(fetchCinemas, equalTo(cinemas));


        verify(cinemaRepository).findBycinemaNameIgnoreCaseContaining("Avengers", pageable);
    }

    @Test
    public void should_return_a_movie_session_when_add_movie_session_given_a_movie_session(){
        // given

        Movie movie = new Movie();
        String movieId = "63a00a4955506136f35be595";
        movie.setMovieId(movieId);
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setMovieSessionIds(new ArrayList<>());
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));
        given(movieRepository.findById(movieId)).willReturn(java.util.Optional.of(movie));


        String cinemaId = "63a00a4955506136f35be596";
        Cinema cinema = new Cinema();
        cinema.setCinemaId(cinemaId);
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());

        given(cinemaRepository.findById(cinemaId)).willReturn(java.util.Optional.of(cinema));


        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setAvailableSeatings(new ArrayList<>());
        movieSession.setCinemaId(cinemaId);
        movieSession.setMovieId(movieId);
        movieSession.setPricing(new ArrayList<>());

        given(movieSessionRepository.save(movieSession)).willReturn(movieSession);


        // when
        MovieSession savedMovieSession = movieService.addMovieSession(movieSession);

        // then
        assertThat(savedMovieSession.getCinemaId(), equalTo(cinemaId));
        assertThat(savedMovieSession.getDatetime(), equalTo(now));
        assertThat(savedMovieSession.getAvailableSeatings(), empty());
        assertThat(savedMovieSession.getMovieId(), equalTo(movieId));
        assertThat(savedMovieSession.getPricing(), empty());

        verify(movieSessionRepository).save(movieSession);
    }

    @Test
    public void should_return_a_movie_session_when_get_movie_session_by_id_given_a_session_id(){
        // given
        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setAvailableSeatings(new ArrayList<>());
        movieSession.setCinemaId("1");
        movieSession.setMovieId("2");
        movieSession.setPricing(new ArrayList<>());

        String movieSessionId = "63a00a4955506136f35be596";


        given(movieSessionRepository.findById(movieSessionId)).willReturn(java.util.Optional.of(movieSession));

        // when
        MovieSession savedMovieSession = movieService.findMovieSessionById(movieSessionId);

        // then
        assertThat(savedMovieSession.getCinemaId(), equalTo("1"));
        assertThat(savedMovieSession.getDatetime(), equalTo(now));
        assertThat(savedMovieSession.getAvailableSeatings(), empty());
        assertThat(savedMovieSession.getMovieId(), equalTo("2"));
        assertThat(savedMovieSession.getPricing(), empty());
        verify(movieSessionRepository).findById(movieSessionId);
    }



}
