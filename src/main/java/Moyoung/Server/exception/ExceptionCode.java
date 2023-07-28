package Moyoung.Server.exception;

import lombok.Getter;

public enum ExceptionCode {
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
