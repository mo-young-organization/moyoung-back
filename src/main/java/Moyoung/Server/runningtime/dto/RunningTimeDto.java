package Moyoung.Server.runningtime.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RunningTimeDto {
    @Getter
    @Builder
    public static class Response {
        private long runningTimeId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
}
