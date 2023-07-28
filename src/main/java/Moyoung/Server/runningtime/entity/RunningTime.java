package Moyoung.Server.runningtime.entity;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RunningTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long runningTimeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
