package Moyoung.Server.runningtime.repository;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RunningTimeRepository extends JpaRepository<RunningTime, Long> {

    List<RunningTime> findRunningTimesByCinemaAndMovieAndStartTimeBetween(
            Cinema cinema,
            Movie movie,
            LocalDateTime startOfDate,
            LocalDateTime endOfDate);
    void deleteAllByRecruitingArticlesIsEmptyAndStartTimeBefore(LocalDateTime localDateTime);

    @Query("SELECT rt FROM RunningTime rt " +
            "WHERE rt.cinema = :cinema " +
            "AND rt.startTime > :startOfDate " +
            "AND rt.startTime < :endOfDate " +
            "AND rt.movie IN (SELECT m FROM Movie m WHERE m.name LIKE %:movieName%)")
    List<RunningTime> findRunningTimesByCinemaAndStartTimeBetweenAndMovieNameContaining(
            @Param("cinema") Cinema cinema,
            @Param("startOfDate") LocalDateTime startOfDate,
            @Param("endOfDate") LocalDateTime endOfDate,
            @Param("movieName") String movieName
    );
}
