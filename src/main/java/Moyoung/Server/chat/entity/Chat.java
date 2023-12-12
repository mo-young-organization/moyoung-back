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
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;
    private LocalDateTime chatTime;
    private String content;
    private Type type = Type.CHAT;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiting_article_id")
    private RecruitingArticle recruitingArticle;

    public Chat() {
    }

    @Builder
    public Chat(long chatId, LocalDateTime chatTime, String content, Type type, Member sender, RecruitingArticle recruitingArticle) {
        this.chatId = chatId;
        this.chatTime = chatTime;
        this.content = content;
        this.type = type;
        this.sender = sender;
        this.recruitingArticle = recruitingArticle;
    }

    public enum Type {
        ENTER, CHAT, EXIT
    }
}
