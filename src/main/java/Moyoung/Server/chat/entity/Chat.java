package Moyoung.Server.chat.entity;

import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiting_article_id")
    private RecruitingArticle recruitingArticle;
}
