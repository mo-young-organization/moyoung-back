package Moyoung.Server.cinema.dto;

import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.runningtime.dto.RunningTimeDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CinemaDto {
    @Getter
    @Builder
    public static class Response {
        private long cinemaId;
        private String brand;
        private String name;
        private String address;
        private double x;
        private double y;
        private List<ScreenInfo> screenInfoList;
    }

    @Getter
    @Builder
    public static class NearResponse {
        private MovieDto.NearResponse movieInfo;
        private List<Response> cinemaInfo;
    }

    @Getter
    @Builder
    public static class ScreenInfo {
        private String screenInfo;
        private List<RunningTimeDto.Response> runningTimeList;
    }
}
