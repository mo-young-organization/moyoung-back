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
    @Setter
    @Builder
    public static class Response {
        private long senderId;
        private String displayName;
        private LocalDateTime chatTime;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class ChatRoomResponse {
        private long recruitingArticleId;
        private String title;
        private int maxNum;
        private int currentNum;
        private String movieThumbnailUrl;
        private String cinemaName;
        private String cinemaBrand;
        private int unreadCount;
        private String lastMessage;
        private LocalDateTime lastMessageAt;
    }

    @Getter
    @Setter
    public static class EnterExit {
        private String sender;
        private String content;
    }
}
