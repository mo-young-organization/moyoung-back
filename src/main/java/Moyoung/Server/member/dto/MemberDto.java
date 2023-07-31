package Moyoung.Server.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberDto {
    @Getter
    @Setter
    public static class Post {
        @NotBlank
        private String id;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class DisplayName {
        @NotBlank
        private String displayName;
    }

    @Getter
    @Setter
    public static class Info {
        @NotBlank
        private String displayName;
        @NotBlank
        private boolean gender;
        @NotBlank
        private int age;
    }
}
