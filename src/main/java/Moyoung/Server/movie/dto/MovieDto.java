package Moyoung.Server.movie.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class MovieDto {
    @Getter
    @Builder
    public static class Response {
        private long movieId;
        private String name;
        private String thumbnailUrl;
    }

    @Getter
    @Builder
    public static class NearResponse {
        private long movieId;
        private String name;
        private String thumbnailUrl;
        private String movieRating;
        private String info;
    }

    @Getter
    @Builder
    public static class RankResponse {
        private LocalDate date;
        private List<Rank> ranks;
    }

    @Getter
    @Builder
    public static class Rank {
        private int rank;
        private String name;
        private String thumbnailRul;
    }
}
