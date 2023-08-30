package Moyoung.Server.runningtime.controller;

import Moyoung.Server.runningtime.crawler.RunningTimeCrawlerService;
import Moyoung.Server.runningtime.service.RunningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RunningTimeController {
    private final RunningTimeCrawlerService runningTimeCrawlerService;

    @PostMapping("/runningMega")
    public ResponseEntity postCrwalMege() throws IOException {
        runningTimeCrawlerService.crawlMegaBox();

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/runningLotte")
    public ResponseEntity postCrwalLotte() throws IOException {
        runningTimeCrawlerService.crawlLotteCinema();

        return new ResponseEntity(HttpStatus.OK);
    }
}
