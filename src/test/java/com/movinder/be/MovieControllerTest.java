package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movinder.be.entity.Movie;
import com.movinder.be.repository.MovieRepository;
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
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    MovieRepository movieRepository;

    @BeforeEach
    public void clearDB() {
        movieRepository.deleteAll();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].thumbnailUrl").value("http://testurl2"));;
    }

}
