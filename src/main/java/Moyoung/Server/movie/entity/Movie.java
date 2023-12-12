package Moyoung.Server.movie.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
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
    private String movieCode;
    private String showTm; // 상영 시간
    private String name;
    private String enName;
    private String thumbnailUrl;
    private String movieRating;
    @Column(length = 1500)
    private String info;
    private String releaseDate; // 개봉일자
    private String genre;
    private String country;
    // LazyInitializationException 방지 EAGER 설정
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<MovieRank> movieRanks = new ArrayList<>();
    private LocalDate lastAddedAt;

    public void addMovieRank(MovieRank movieRank) {
        this.movieRanks.add(movieRank);
        if (movieRank.getMovie() != this) {
            movieRank.setMovie(this);
        }
    }
    public void addCountry(String country) {
        if (this.country != null) {
            this.country = this.country + ", " + country;
        } else this.country = country;
    }
    public void addGenre(String genre) {
        if (this.genre != null) {
            this.genre = this.genre + ", " + genre;
        } else this.genre = genre;
    }
}
