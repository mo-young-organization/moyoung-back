package Moyoung.Server.runningtime.controller;

import Moyoung.Server.runningtime.service.RunningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RunningTimeController {
    private final RunningTimeService runningTimeService;
}
