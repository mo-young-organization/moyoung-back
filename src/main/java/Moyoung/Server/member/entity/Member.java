package Moyoung.Server.member.entity;

import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    private String id;
    private String password;
    private String displayName;
    private Age age = Age.NON;
    private Boolean gender;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    List<RecruitingArticle> recruitingArticleList = new ArrayList<>();

    public enum Age {
        NON("설정이 필요합니다."),
        TEENAGER("10대"),
        TWENTIES("20대"),
        THIRTIES("30대 이상");

        @Getter
        private String age;
        Age(String age) {
            this.age = age;
        }
    }

    // 생성자
    public Member(String id) {
        this.id = id;
    }
}
