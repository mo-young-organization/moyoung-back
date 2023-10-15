package Moyoung.Server.chat.dto;

import lombok.*;

import java.time.LocalDateTime;


public class ChatDto {
    @Getter
    public static class Send {
        private long senderId;
        private String message;
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
