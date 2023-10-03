package Moyoung.Server.recruitingarticle.entity;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomMembersInfo;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.runningtime.entity.RunningTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JsonManagedReference // 무한 재귀 방지
    @OneToMany(mappedBy = "recruitingArticle", cascade = CascadeType.ALL)
    private List<ChatRoomMembersInfo> participants = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "running_time_id")
    private RunningTime runningTime;
    @OneToMany(mappedBy = "recruitingArticle")
    private List<Chat> chats = new ArrayList<>();

    private String title;
    // 참여 최대 인원
    private int maxNum;
    private int currentNum;
    private Age age;
    private Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String lastChat;
    private LocalDateTime lastChatCreated;

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
