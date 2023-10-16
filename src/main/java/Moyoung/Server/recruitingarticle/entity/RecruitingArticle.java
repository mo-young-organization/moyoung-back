package Moyoung.Server.recruitingarticle.entity;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomInfo;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.runningtime.entity.RunningTime;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "running_time_id")
    private RunningTime runningTime;

    @OneToMany(mappedBy = "recruitingArticle")
    private List<ChatRoomInfo> chatRoomInfos = new ArrayList<>();

    @OneToMany(mappedBy = "recruitingArticle", fetch = FetchType.LAZY)
    private List<Chat> chats = new ArrayList<>();

    private String title;
    // 참여 최대 인원
    private int maxNum;
    private int currentNum;
    private Age age;
    private Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 추가 정보 비정규화
    private String cinemaRegion;
    private String cinemaName;
    private String movieName;
    private String movieThumbnailUrl;

    public void addChatRoomInfo(ChatRoomInfo chatRoomInfo) {
        this.chatRoomInfos.add(chatRoomInfo);
        if (chatRoomInfo.getRecruitingArticle() != this) {
            chatRoomInfo.setRecruitingArticle(this);
        }
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
