package Moyoung.Server.recruitingarticle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class RecruitingArticleDto {
    @Getter
    @Setter
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
        private List<Integer> ages;
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
        private List<String> ages;
    }

    @Getter
    @Builder
    public static class Response {
        private long recruitingArticleId;
        private Long writerMemberId;
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
        private List<String> ages;
        private List<UserInfo> userInfos;
    }

    @Getter
    @Builder
    public static class UserInfo {
        private Long memberId;
        private String gender;
        private String displayName;
        private String age;
    }
}
