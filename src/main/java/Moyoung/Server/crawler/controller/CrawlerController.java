package Moyoung.Server.crawler.controller;

import Moyoung.Server.crawler.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CrawlerController {
    private final CrawlerService crawlerService;

    @PostMapping("/manual/re-crawl")
    public ResponseEntity manualReCrawl() throws IOException {
        crawlerService.reCrawlTodayRunningTime();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
