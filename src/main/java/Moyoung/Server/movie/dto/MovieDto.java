package Moyoung.Server.movie.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class MovieDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String name;
        private String enName;
        private String movieRating;
        private String thumbnailUrl;
        private String movieInfo;
        public List<Type> typeList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Type {
        private long movieId;
        private String type;
        private int count;
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
        private String thumbnailUrl;
    }
}
