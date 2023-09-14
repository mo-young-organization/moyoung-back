package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String name);

    Page<Movie> findAllByNameContains(String name, Pageable pageable);
}
