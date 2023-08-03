package Moyoung.Server.recruitingarticle.entity;

import Moyoung.Server.chat.entity.Chat;
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
    @OneToMany(mappedBy = "recruitingArticle")
    private List<Chat> chats;

    private String title;
    // 참여 최대 인원
    private int maxNum;
    private Age age;
    private Gender gender;

    public void addParticipant(Member member) {
        this.participants.add(member);
    }
    public enum Age {
        TEENAGER("10대"),
        TWENTIES("20대"),
        THIRTIES("30대 이상");

        @Getter
        private String age;
        Age(String age) {
            this.age = age;
        }
    }
    public enum Gender {
        ALL("전체"),
        MAN("남자만"),
        WOMAN("여자만");

        @Getter
        private String explain;
        Gender(String explain) {
            this.explain = explain;
        }
    }
}
