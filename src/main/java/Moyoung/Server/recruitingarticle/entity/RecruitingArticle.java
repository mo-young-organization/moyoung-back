package Moyoung.Server.recruitingarticle.entity;

import Moyoung.Server.member.entity.Member;
import Moyoung.Server.runningtime.entity.RunningTime;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class RecruitingArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recruitingArticleId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany
    private List<Member> participants;
    @ManyToOne
    @JoinColumn(name = "running_time_id")
    private RunningTime runningTime;

    public void addParticipant(Member member) {
        this.participants.add(member);
    }
}
