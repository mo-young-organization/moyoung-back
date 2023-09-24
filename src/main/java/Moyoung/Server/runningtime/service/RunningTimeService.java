package Moyoung.Server.runningtime.service;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RunningTimeService {
    private final RunningTimeRepository runningTimeRepository;

    public List<RunningTime> find(Cinema cinema, Movie movie, boolean early, LocalDate date) {
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(23, 59, 59);

        if (early) return runningTimeRepository.findRunningTimesByCinemaAndMovieAndStartTimeBetweenAndEarlyMorning(cinema, movie, startOfDate, endOfDate, true);
        else return runningTimeRepository.findRunningTimesByCinemaAndMovieAndStartTimeBetween(cinema, movie, startOfDate, endOfDate);
    }

    public RunningTime findVerifiedRunningTime(long runningTimeId) {
        Optional<RunningTime> optionalRunningTime = runningTimeRepository.findById(runningTimeId);
        RunningTime runningTime = optionalRunningTime.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return runningTime;
    }
}
