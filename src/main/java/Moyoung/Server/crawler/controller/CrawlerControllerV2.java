package Moyoung.Server.crawler.controller;

import Moyoung.Server.crawler.service.CrawlerServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/crawler")
public class CrawlerControllerV2 {
    private final CrawlerServiceV2 crawlerServiceV2;

    @PostMapping("/cinema")
    public ResponseEntity postCinema() {
        crawlerServiceV2.crawlCinemaXY();

        return new ResponseEntity(HttpStatus.OK);
    }
}
