package Moyoung.Server.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    @Setter
    public static class EnterExit {
        private String sender;
        private String content;
    }
}
