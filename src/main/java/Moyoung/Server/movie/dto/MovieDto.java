package Moyoung.Server.movie.dto;

import lombok.Builder;
import lombok.Getter;

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
}
