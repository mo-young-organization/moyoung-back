package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepositoryCustom {
    Optional<Movie> findByName(String name);
    List<Movie> findAllByNameContains(String name);
}
