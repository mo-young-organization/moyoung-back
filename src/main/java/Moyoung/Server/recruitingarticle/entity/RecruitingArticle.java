package Moyoung.Server.recruitingarticle.entity;

import Moyoung.Server.member.entity.Member;
import Moyoung.Server.runningtime.entity.RunningTime;

import javax.persistence.*;

@Entity
public class RecruitingArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recruitingArticleId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "running_time_id")
    private RunningTime runningTime;
}
