package Moyoung.Server.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ChatDto {
    @Getter
    public static class Post {
        private String content;
    }

    @Getter
    @Builder
    public static class Response {
        private long senderId;
        private String displayName;
        private LocalDateTime chatTime;
        private String content;
    }
}
