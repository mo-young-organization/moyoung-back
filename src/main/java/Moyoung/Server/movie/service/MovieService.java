package Moyoung.Server.movie.service;

import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.movie.repository.MovieRankRepository;
import Moyoung.Server.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieRankRepository movieRankRepository;

    public Movie findMovie(long movieId) {
        return findVerifiedMovie(movieId);
    }

    public List<MovieRank> findMovieRankByDate(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }

        List<MovieRank> movieRankList = movieRankRepository.findAllByDate(date.minusDays(1));

        if (movieRankList.isEmpty()) {
            return movieRankRepository.findAllByDate(date.minusDays(2));
        }

        return movieRankList;
    }
  
    private Movie findVerifiedMovie(long movieId) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);

        return optionalMovie.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MOVIE_NOT_FOUND));
    }
}
