package com.movinder.be;

import com.movinder.be.entity.Movie;
import com.movinder.be.repository.MovieRepository;
import com.movinder.be.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class MovieServiceTest {

    @Mock
    MovieRepository movieRepository;

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
}
