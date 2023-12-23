package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.MovieRank;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieRankRepository extends JpaRepository<MovieRank, Long>, MovieRankRepositoryCustom{
}
