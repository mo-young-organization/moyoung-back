package Moyoung.Server.movie.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class MovieRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long movieRankId;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private int movieRank;
    private LocalDate date;

    public void setMovie(Movie movie) {
        this.movie = movie;
        if (!movie.getMovieRanks().contains(this)) {
            movie.addMovieRank(this);
        }
    }
}
