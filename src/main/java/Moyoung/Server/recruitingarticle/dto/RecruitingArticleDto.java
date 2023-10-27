package Moyoung.Server.recruitingarticle.dto;

import Moyoung.Server.runningtime.dto.RunningTimeDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RecruitingArticleDto {
    @Getter
    public static class PostPatch {
        @NotBlank
        private String title;
        @NotBlank
        private long runningTimeId;
        @NotBlank
        private int maxNum;
        @NotBlank
        private int gender;
        @NotBlank
        private int age;
    }

    @Getter
    @Builder
    public static class ResponseForList {
        private long recruitingArticleId;
        private String writerDisplayName;
        private String writerAge;
        private String writerGender;
        private String title;
        private String cinemaRegion;
        private String cinemaName;
        private String cinemaBrand;
        private String movieThumbnailUrl;
        private String movieName;
        private String movieRating;
        private LocalDateTime startTime;
        private String screenInfo;
        private int maxNum;
        private int currentNum;
        private String gender;
        private String age;
    }
}
