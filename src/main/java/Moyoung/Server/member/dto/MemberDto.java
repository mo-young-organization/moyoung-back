package Moyoung.Server.member.dto;

import Moyoung.Server.validator.NotSpace;
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
    public static class PostInfo {
        @NotBlank
        @Pattern(regexp = "^[A-Za-z가-힣]{2,5}$|^$")
        private String displayName;
        @NotNull
        private Boolean gender;
        @NotNull
        private Integer age;
    }

    @Getter
    @Setter
    public static class PatchInfo {
        @NotSpace
        @Pattern(regexp = "^[A-Za-z가-힣]{2,5}$|^$")
        private String displayName;
        private Boolean gender;
        private Integer age;
    }



    @Getter
    @Setter
    public static class MemberId {
        private long memberId;
    }
}
