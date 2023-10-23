package Moyoung.Server.runningtime.service;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RunningTimeService {
    private final RunningTimeRepository runningTimeRepository;

    public List<RunningTime> find(Cinema cinema, Movie movie, LocalDate date) {
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(23, 59, 59);

        return runningTimeRepository.findRunningTimesByCinemaAndMovieAndStartTimeBetween(cinema, movie, startOfDate, endOfDate);
    }

    public RunningTime findVerifiedRunningTime(long runningTimeId) {
        Optional<RunningTime> optionalRunningTime = runningTimeRepository.findById(runningTimeId);
        RunningTime runningTime = optionalRunningTime.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return runningTime;
    }

    // 상영시간 자동 삭제
    @Scheduled(cron = "0 1 0 * * *") // 매일 0시 1분 0초 실행
    public void deleteRunningTime() {
        runningTimeRepository.deleteAllByRecruitingArticlesIsEmptyAndStartTimeBefore(LocalDateTime.now().minusDays(2L));
    }
}
