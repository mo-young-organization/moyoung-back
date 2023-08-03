package Moyoung.Server.recruitingarticle.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class RecruitingArticleDto {
    @Getter
    public static class Post {
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
}
