package Moyoung.Server.crawler.controller;

import Moyoung.Server.crawler.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawler")
public class CrawlerController {
    private final CrawlerService crawlerService;

    @PostMapping("/mega")
    public ResponseEntity postCrwalMega() throws IOException {
        crawlerService.crawlMegaBox();

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/lotte")
    public ResponseEntity postCrwalLotte() throws IOException {
        crawlerService.crawlLotteCinema();

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/movie-rank")
    public ResponseEntity postMovieRank() throws IOException {
        crawlerService.insertMovieRank();

        return new ResponseEntity(HttpStatus.OK);
    }
}
