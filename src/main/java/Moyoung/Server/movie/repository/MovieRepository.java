package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String name);
    List<Movie> findAllByNameContains(String name);
    Page<Movie> findAllByNameContainsAndLastAddedAtAfter(String name, LocalDate date, Pageable pageable);
}
