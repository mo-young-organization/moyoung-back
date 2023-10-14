package Moyoung.Server.movie.mapper;

import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.entity.MovieRank;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto.Response movieToResponse(Movie movie);

    default List<MovieDto.Response> moviesToResponses(List<Movie> movies) {
        return movies.stream()
                .map(movie -> movieToResponse(movie))
                .collect(Collectors.toList());
    }

    default MovieDto.RankResponse moviesToRankResponses(List<MovieRank> movieRanks, LocalDate date) {
        return MovieDto.RankResponse.builder()
                .date(date)
                .ranks(movieRanks.stream().map(movieRank -> movieRankToRank(movieRank)).collect(Collectors.toList())).build();
    }

    default MovieDto.Rank movieRankToRank(MovieRank movieRank) {
        Movie movie = movieRank.getMovie();
        String movieName = movie.getName();
        movieName = movieName.replaceAll("\\[.*?\\]", "");
        movieName = movieName.trim();

        return MovieDto.Rank.builder()
                .rank(movieRank.getMovieRank())
                .name(movieName)
                .thumbnailUrl(movie.getThumbnailUrl()).build();
    }
}
