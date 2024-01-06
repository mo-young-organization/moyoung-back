package Moyoung.Server.runningtime.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class RunningTimeDto {
    @Getter
    @Builder
    public static class Response {
        private long runningTimeId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    @Getter
    @Builder
    public static class ArticleListResponse {
        private long recruitingArticleId;
        private String title;
        private int maxNum;
        private int currentNum;
        private String gender;
        private List<String> ages;
    }
}
