package Moyoung.Server.movie.service;

import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Movie findMovie(long movieId) {
        return findVerifiedMovie(movieId);
    }

    public Page<Movie> findMoviesByName(String movieName, Integer page) {
        if (page == null) page = 1;
        return movieRepository.findAllByNameContains(movieName, PageRequest.of(page - 1, 25, Sort.by("movieId").descending()));
    }

    private Movie findVerifiedMovie(long movieId) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);

        return optionalMovie.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MOVIE_NOT_FOUND));
    }
}
