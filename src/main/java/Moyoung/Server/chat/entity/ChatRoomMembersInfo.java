package Moyoung.Server.chat.entity;

import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ChatRoomMembersInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomMembersInfoId;
    private int unreadMessageCount;
    private Long lastMessageId;
    private String sessionId;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "recruiting_article_id")
    private RecruitingArticle recruitingArticle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;
}
