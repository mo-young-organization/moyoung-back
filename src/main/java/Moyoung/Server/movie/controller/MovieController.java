package Moyoung.Server.movie.controller;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.service.CinemaService;
import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.movie.mapper.MovieMapper;
import Moyoung.Server.movie.service.MovieService;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.service.RunningTimeService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final CinemaService cinemaService;
    private final RunningTimeService runningTimeService;
    private final MovieMapper movieMapper;

    @GetMapping("/movie")
    public ResponseEntity getMovie(@RequestParam String movieName,
                                   @RequestParam double x,
                                   @RequestParam double y,
                                   @RequestParam double distance) {
        if (StringUtils.isBlank(movieName)) {
            return new ResponseEntity<>("movieName 파라미터는 비어 있을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<Cinema> cinemaList = cinemaService.findCinemaList(x, y, distance);
        Map<String, List<RunningTime>> moviesMap = runningTimeService.findDistinctedRunningTimeWithMovies(cinemaList,URLDecoder.decode(movieName, StandardCharsets.UTF_8));

        return new ResponseEntity<>(movieMapper.moviesToResponses(moviesMap), HttpStatus.OK);
    }

    @GetMapping("/movie/rank")
    public ResponseEntity getMovieRank(@RequestParam(required = false)LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<MovieRank> movieRankList = movieService.findMovieRankByDate(date);

        return new ResponseEntity(movieMapper.moviesToRankResponses(movieRankList, date), HttpStatus.OK);
    }
}
