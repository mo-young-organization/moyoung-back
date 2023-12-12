package Moyoung.Server.runningtime.entity;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class RunningTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long runningTimeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String screenInfo;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @OneToMany(mappedBy = "runningTime", cascade = CascadeType.PERSIST)
    private List<RecruitingArticle> recruitingArticles = new ArrayList<>();

    public void addRecruitingArticle(RecruitingArticle recruitingArticle) {
        this.recruitingArticles.add(recruitingArticle);

        if (recruitingArticle.getRunningTime() != this) {
            recruitingArticle.setRunningTime(this);
        }
    }
}
