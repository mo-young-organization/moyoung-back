package Moyoung.Server.runningtime.repository;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RunningTimeRepository extends JpaRepository<RunningTime, Long> {

    List<RunningTime> findRunningTimesByCinemaAndMovieAndStartTimeBetween(
            Cinema cinema,
            Movie movie,
            LocalDateTime startOfDate,
            LocalDateTime endOfDate);
    void deleteAllByRecruitingArticlesIsEmptyAndStartTimeBefore(LocalDateTime localDateTime);
}
