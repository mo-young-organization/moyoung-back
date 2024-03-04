package Moyoung.Server.runningtime.controller;

import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.mapper.RunningTimeMapper;
import Moyoung.Server.runningtime.service.RunningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RunningTimeController {
    private final RunningTimeService runningTimeService;
    private final RunningTimeMapper runningTimeMapper;

    @GetMapping("runningTime/{runningTime-id}/article")
    public ResponseEntity getArticleListInRunningTime(@PathVariable("runningTime-id") long runningTimeId) {
        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(runningTimeId);

        return new ResponseEntity<>(runningTimeMapper.runningTimeToRunningTimeListResponse(runningTime), HttpStatus.OK);
    }
}
