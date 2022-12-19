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

}
