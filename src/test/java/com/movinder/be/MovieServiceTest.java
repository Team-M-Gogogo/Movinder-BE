package com.movinder.be;

import com.movinder.be.entity.Movie;
import com.movinder.be.repository.MovieRepository;
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
}
