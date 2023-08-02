package Moyoung.Server.cinema.controller;

import Moyoung.Server.cinema.service.CinemaCrawlerService;
import Moyoung.Server.cinema.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;
    private final CinemaCrawlerService cinemaCrawlerService;

    @PostMapping("/cinema")
    public ResponseEntity postCinema() throws IOException {
        return new ResponseEntity(cinemaCrawlerService.crawlCgvCinemaList(), HttpStatus.OK);
    }
}
