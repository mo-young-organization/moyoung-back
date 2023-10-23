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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Map<String, List<RunningTime>> findDistinctedRunningTimeWithMovies(List<Cinema> cinemaList, String movieName) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(23, 59, 59);

        List<RunningTime> allRunningTimes = cinemaList.stream()
                .map(cinema -> runningTimeRepository.findRunningTimesByCinemaAndStartTimeBetweenAndMovieNameContaining(cinema, startOfDate, endOfDate, movieName))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 중복된 CinemaId와 MovieId를 기준으로 중복을 제거
        List<RunningTime> distinctRunningTimes = allRunningTimes.stream()
                .collect(Collectors.toMap(
                        runningTime -> runningTime.getCinema().getCinemaId() + "-" + runningTime.getMovie().getMovieId(),
                        runningTime -> runningTime,
                        (existing, replacement) -> existing)
                )
                .values()
                .stream()
                .collect(Collectors.toList());

        // 중복된 RunningTime을 영화 이름을 기준으로 그룹화
        Map<String, List<RunningTime>> distinctRunningTimesMap = distinctRunningTimes.stream()
                .collect(Collectors.groupingBy(
                        runningTime -> removeParentheses(runningTime.getMovie().getName()) // 중복 기준은 패턴이 제거된 영화 이름
                ));

        return distinctRunningTimesMap;
    }


    // 괄호와 내용을 제거
    private static String removeParentheses(String name) {
        return name.replaceAll("\\(.*?\\)", "").trim();
    }

    // 상영시간 자동 삭제
    @Scheduled(cron = "0 0 2 * * *") // 매일 2시 0분 0초 실행
    public void deleteRunningTime() {
        runningTimeRepository.deleteAllByRecruitingArticlesIsEmptyAndStartTimeBefore(LocalDateTime.now().minusDays(2L));
    }
}
