package Moyoung.Server.runningtime.entity;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Boolean earlyMorning;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
