package Moyoung.Server.runningtime.repository;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.entity.RunningTime;

import java.time.LocalDateTime;
import java.util.List;

public interface RunningTimeRepositoryCustom {
    List<RunningTime> findRunningTimesByCinemaAndStartTimeBetweenAndMovieNameContaining(
            Cinema cinema,
            LocalDateTime startOfDate,
            LocalDateTime endOfDate,
            String movieName);

    List<RunningTime> findRunningTimesByCinemaAndMovieAndStartTimeBetween(
            Cinema cinema, Movie movie, LocalDateTime startOfDate, LocalDateTime endOfDate);

    void deleteAllByRecruitingArticlesIsEmptyAndStartTimeBefore(LocalDateTime localDateTime);
}
