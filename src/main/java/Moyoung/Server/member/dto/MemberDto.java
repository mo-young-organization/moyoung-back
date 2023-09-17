package Moyoung.Server.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
        @Pattern(regexp = "^[A-Za-z가-힣]{2,5}$|^$")
        private String displayName;
    }

    @Getter
    @Setter
    public static class Info {
        @NotBlank
        @Pattern(regexp = "^[A-Za-z가-힣]{2,5}$|^$")
        private String displayName;
        @NotNull
        private Boolean gender;
        @NotNull
        private Integer age;
    }
}
