package Moyoung.Server.runningtime.service;

import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RunningTimeService {
    private final RunningTimeRepository runningTimeRepository;

    public RunningTime findVerifiedRunningTime(long runningTimeId) {
        Optional<RunningTime> optionalRunningTime = runningTimeRepository.findById(runningTimeId);
        RunningTime runningTime = optionalRunningTime.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return runningTime;
    }
}
