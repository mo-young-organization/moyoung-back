package Moyoung.Server.chat.entity;

import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;
    @Enumerated(value = EnumType.STRING)
    private ContentType type;
    private LocalDateTime createdAt;
    private String content;
    private String sender;
    private int unreadCount;

    @ManyToOne
    @JoinColumn(name = "recruiting_article_id")
    private RecruitingArticle recruitingArticle;

    public enum ContentType {
        ENTER, TALK, LEAVE, REENTER, NOTICE, DISCONNECTED, BAN
    }

}
