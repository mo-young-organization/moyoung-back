package Moyoung.Server.runningtime.service;

import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunningTimeService {
    private final RunningTimeRepository runningTimeRepository;
}
