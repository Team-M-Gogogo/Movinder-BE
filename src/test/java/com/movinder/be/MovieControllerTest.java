package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.movinder.be.controller.dto.BookingRequest;
import com.movinder.be.controller.dto.RequestItem;
import com.movinder.be.entity.*;
import com.movinder.be.repository.*;
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
import java.util.Collections;

import static org.assertj.core.util.Arrays.sizeOf;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FoodRepository foodRepository;

    @BeforeEach
    public void clearDB() {
        movieRepository.deleteAll();
        cinemaRepository.deleteAll();
        customerRepository.deleteAll();
        movieSessionRepository.deleteAll();
    }

    // test
    @Test
    public void should_create_movie_when_perform_create_given_movie_obj() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setTrailerUrl("https://www.youtube.com/watch?v=GqWbeWBCBXs&ab_channel=EasonChan");

        String movieJson = new ObjectMapper().writeValueAsString(movie);


        //when & then
        client.perform(MockMvcRequestBuilders.post("/movie/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieName").value("Avengers"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Action movie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastShowDateTime").value("1970-01-01T00:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieSessionIds").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailUrl").value("http://testurl"));
    }

    // get all film
    @Test
    public void should_get_movie_list_when_perform_get_given_params() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(1, 0, ZoneOffset.ofHours(0)));

        Movie savedMovie = movieRepository.save(movie);

        Movie movie2 = new Movie();
        movie2.setMovieName("Avengers 2");
        movie2.setDescription("Action movie");
        movie2.setDuration(110);
        movie2.setThumbnailUrl("http://testurl2");
        movie2.setLastShowDateTime(LocalDateTime.ofEpochSecond(2, 0, ZoneOffset.ofHours(0)));
        movieRepository.save(movie2);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/films?from=1970-01-01T00:00:00&to=1970-01-02T00:00:01", savedMovie.getMovieId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieName").value("Avengers"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Action movie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastShowDateTime").value("1970-01-01T00:00:01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieSessionIds").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].thumbnailUrl").value("http://testurl"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].movieId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].movieName").value("Avengers 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Action movie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].duration").value(110))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastShowDateTime").value("1970-01-01T00:00:02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].movieSessionIds").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].thumbnailUrl").value("http://testurl2"));
    }

    @Test
    public void should_get_movie_when_perform_get_given_film_id() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));

        Movie savedMovie = movieRepository.save(movie);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/films/{id}", savedMovie.getMovieId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieName").value("Avengers"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Action movie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastShowDateTime").value("1970-01-01T00:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieSessionIds").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailUrl").value("http://testurl"));
    }

    @Test
    public void should_create_cinema_when_perform_create_given_cinema_obj() throws Exception {
        // given
        Cinema cinema = new Cinema();
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());


        String cinemaJson = new ObjectMapper().writeValueAsString(cinema);


        //when & then
        client.perform(MockMvcRequestBuilders.post("/movie/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cinemaJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaName").value("MCL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("address"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.floorPlan").isEmpty());
    }

    @Test
    public void should_get_cinema_when_perform_get_given_cinema_id() throws Exception {
        //given
        Cinema cinema = new Cinema();
        String cinemaId = "63a00a4955506136f35be595";
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());

        Cinema savedCinema = cinemaRepository.save(cinema);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/cinemas/{id}", savedCinema.getCinemaId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaName").value("MCL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("address"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.floorPlan").isEmpty());
    }

    @Test
    public void should_get_cinema_list_when_perform_get_given_params_with_filter() throws Exception {
        //given
        Cinema cinema1 = new Cinema();
        cinema1.setAddress("address");
        cinema1.setCinemaName("MCL");
        cinema1.setFloorPlan(new ArrayList<>());

        Cinema savedCinema = cinemaRepository.save(cinema1);



        Cinema cinema2 = new Cinema();
        cinema2.setAddress("address 2");
        cinema2.setCinemaName("MCL 2");
        cinema2.setFloorPlan(new ArrayList<>());
        Cinema savedCinema2 = cinemaRepository.save(cinema2);

        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/cinemas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaName").value("MCL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("address"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].floorPlan").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cinemaId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cinemaName").value("MCL 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].address").value("address 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].floorPlan").isEmpty());

        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/cinemas?cinemaName={name}", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaName").value("MCL 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("address 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].floorPlan").isEmpty());

    }

    @Test
    public void should_create_movie_session_when_perform_create_given_movie_session_obj() throws Exception {
        //given
        Cinema cinema = new Cinema();
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());
        Cinema savedCinema = cinemaRepository.save(cinema);

        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setMovieSessionIds(new ArrayList<>());
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));

        Movie savedMovie = movieRepository.save(movie);

        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setCinemaId(savedCinema.getCinemaId());
        movieSession.setMovieId(savedMovie.getMovieId());
        movieSession.setPricing(new ArrayList<>());

        String movieSessionJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(movieSession);
        System.out.println("tester");
        System.out.println(movieSessionJson);

        movieRepository.findById(savedMovie.getMovieId());


        //when & then
        client.perform(MockMvcRequestBuilders.post("/movie/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.datetime").value(start))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availableSeatings").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaId").value(savedCinema.getCinemaId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieId").value(savedMovie.getMovieId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricing").isEmpty());
    }

    @Test
    public void should_get_movie_session_when_perform_get_by_id_given_session_id() throws Exception {
        //given
        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setAvailableSeatings(new ArrayList<>());
        movieSession.setCinemaId("1");
        movieSession.setMovieId("2");
        movieSession.setPricing(new ArrayList<>());
        MovieSession savedMovieSession = movieSessionRepository.save(movieSession);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/sessions/{id}", savedMovieSession.getSessionId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.datetime").value(start))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availableSeatings").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricing").isEmpty());
    }

    @Test
    public void should_get_movie_session_when_perform_get_given_film_id_and_cinema_id() throws Exception {
        //given
        String movieId = "63a00a4955506136f35be596";
        String cinemaId = "63a00a4955506136f35be597";
        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setAvailableSeatings(new ArrayList<>());
        movieSession.setCinemaId(cinemaId);
        movieSession.setMovieId(movieId);
        movieSession.setPricing(new ArrayList<>());
        MovieSession savedMovieSession = movieSessionRepository.save(movieSession);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/sessions?filmID={filmID}&cinemaID={cinemaID}", movieId, cinemaId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sessionId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].datetime").value(start))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].availableSeatings").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaId").value(cinemaId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieId").value(movieId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pricing").isEmpty());
    }

    @Test
    public void should_get_Cinema_when_perform_get_cinema_by_movie_id_given_movie_id() throws Exception {
        //given
        Cinema cinema = new Cinema();
        cinema.setAddress("address");
        cinema.setCinemaName("MCL");
        cinema.setFloorPlan(new ArrayList<>());

        Cinema savedCinema = cinemaRepository.save(cinema);

        Movie movie = new Movie();
        movie.setMovieName("Avengers");
        movie.setDescription("Action movie");
        movie.setDuration(100);
        movie.setThumbnailUrl("http://testurl");
        movie.setMovieSessionIds(new ArrayList<>());
        movie.setLastShowDateTime(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0)));

        Movie savedMovie = movieRepository.save(movie);


        String movieId = savedMovie.getMovieId();
        MovieSession movieSession = new MovieSession();
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        movieSession.setDatetime(now);
        movieSession.setAvailableSeatings(new ArrayList<>());
        movieSession.setCinemaId(savedCinema.getCinemaId());
        movieSession.setMovieId(movieId);
        movieSession.setPricing(new ArrayList<>());
        MovieSession savedMovieSession = movieSessionRepository.save(movieSession);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/movie/films/{filmID}/cinemas", movieId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cinemaName").value("MCL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("address"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].floorPlan").isEmpty());
    }




}
