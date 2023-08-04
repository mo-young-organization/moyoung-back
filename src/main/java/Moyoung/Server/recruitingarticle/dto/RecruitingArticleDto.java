package Moyoung.Server.recruitingarticle.dto;

import Moyoung.Server.runningtime.dto.RunningTimeDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

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
        private String title;
        private int maxNum;
        private int currentNum;
        private String gender;
        private String age;
        private RunningTimeDto.Response runningTimeInfo;
    }
}
