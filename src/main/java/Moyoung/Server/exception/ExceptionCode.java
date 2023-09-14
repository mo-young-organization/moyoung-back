package Moyoung.Server.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다"),
    MEMBER_ID_EXISTS(409, "현재 사용중인 아이디 입니다."),
    RECRUIT_ARTICLE_NOT_FOUND(404, "게시글을 찾을 수 없습니다"),
    MOVIE_NOT_FOUND(404, "영화를 찾을 수 없습니다"),
    UNAUTHORIZED(404, "권한이 없습니다" );
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
