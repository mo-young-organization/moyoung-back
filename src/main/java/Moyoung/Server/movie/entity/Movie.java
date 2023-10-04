package Moyoung.Server.movie.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long movieId;
    private String name;
    private String thumbnailUrl;
    private String movieRating;
    @Column(length = 1500)
    private String info;
    private String releaseDate;
    private String genre;
    private String runningTime;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    List<MovieRank> movieRanks = new ArrayList<>();
    private LocalDate lastAddedAt;

    public void addMovieRank(MovieRank movieRank) {
        this.movieRanks.add(movieRank);
        if (movieRank.getMovie() != this) {
            movieRank.setMovie(this);
        }
    }
}
