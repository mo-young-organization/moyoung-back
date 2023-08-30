package Moyoung.Server.runningtime.dto;

import Moyoung.Server.cinema.dto.CinemaDto;
import Moyoung.Server.movie.dto.MovieDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RunningTimeDto {
    @Getter
    @Builder
    public static class Response {
        private long runningTimeId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private MovieDto.Response movieInfo;
        private CinemaDto.Response cinemaInfo;
    }
}
