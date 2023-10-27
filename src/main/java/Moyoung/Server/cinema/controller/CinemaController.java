package Moyoung.Server.cinema.controller;

import Moyoung.Server.cinema.entity.CinemaPlus;
import Moyoung.Server.cinema.mapper.CinemaMapper;
import Moyoung.Server.cinema.service.CinemaService;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final CinemaMapper cinemaMapper;

    @GetMapping("/near")
    public ResponseEntity getNearCinema(@RequestParam double x,
                                        @RequestParam double y,
                                        @RequestParam double distance,
                                        @RequestParam(required = false) boolean mega,
                                        @RequestParam(required = false) boolean lotte,
                                        @RequestParam(required = false) boolean cgv,
                                        @RequestParam long movieId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Movie movie = movieService.findMovie(movieId);
        List<CinemaPlus> cinemaPlusList = cinemaService
                .find(x, y, distance, mega, lotte, cgv, movie, date);

        return new ResponseEntity(cinemaMapper.cinemaPlusListToNearResponse(movie, cinemaPlusList), HttpStatus.OK);
    }
}
