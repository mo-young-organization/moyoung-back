package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.MovieRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieRankRepository extends JpaRepository<MovieRank, Long> {
    List<MovieRank> findAllByDate(LocalDate date);
}
