package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.MovieRank;

import java.time.LocalDate;
import java.util.List;

public interface MovieRankRepositoryCustom {
    List<MovieRank> findAllByDate(LocalDate date);
}
