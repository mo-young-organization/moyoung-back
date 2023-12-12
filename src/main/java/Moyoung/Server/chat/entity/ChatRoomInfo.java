package Moyoung.Server.chat.entity;

import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ChatRoomInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomInfoId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "recruiting_article_id")
    private RecruitingArticle recruitingArticle;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDateTime entryTime;
    private int unreadCount = 0;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public ChatRoomInfo() {
    }
    @Builder
    public ChatRoomInfo(RecruitingArticle recruitingArticle, Member member, LocalDateTime entryTime, LocalDateTime lastMessageAt) {
        this.recruitingArticle = recruitingArticle;
        this.member = member;
        this.entryTime = entryTime;
        this.lastMessageAt = lastMessageAt;
    }

    public void resetUnreadCount() {
        this.unreadCount = 0;
    }

    public void plusUnreadCount() {
        this.unreadCount = this.unreadCount + 1;
    }
}
