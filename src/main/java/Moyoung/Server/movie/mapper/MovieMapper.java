package Moyoung.Server.movie.mapper;

import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.Movie;
import org.mapstruct.Mapper;

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
}
