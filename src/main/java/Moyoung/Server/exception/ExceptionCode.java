package Moyoung.Server.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다"),
    MEMBER_ID_EXISTS(409, "현재 사용중인 아이디 입니다."),
    RECRUIT_ARTICLE_NOT_FOUND(404, "게시글을 찾을 수 없습니다"),
    NOT_ENTER_HERE(403, "게시글에 참여하지 않은 사용자입니다."),
    UNAUTHORIZED(404, "권한이 없습니다" ),
    MOVIE_NOT_FOUND(404, "영화를 찾을 수 없습니다."),
    CAN_NOT_ENTER(404, "최대 인원을 초과하여 입장할 수 없습니다."),
    NOT_LOGIN(402, "로그인이 필요한 서비스 입니다."),
    ALREADY_ENTERED(409, "이미 등록되어 있는 회원입니다."),
    ONLY_AUTHOR(404, "권한이 없습니다.");
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
