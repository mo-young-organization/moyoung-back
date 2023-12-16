package Moyoung.Server.cinema.mapper;

import Moyoung.Server.cinema.dto.CinemaDto;
import Moyoung.Server.cinema.entity.CinemaPlus;
import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.dto.RunningTimeDto;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.mapstruct.Mapper;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    default CinemaDto.NearResponse cinemaPlusListToNearResponse(Movie movie, List<CinemaPlus> cinemaPlusList) {
        return CinemaDto.NearResponse.builder()
                .movieInfo(MovieDto.NearResponse.builder()
                        .movieId(movie.getMovieId())
                        .name(movie.getName())
                        .enName(movie.getEnName())
                        .thumbnailUrl(movie.getThumbnailUrl())
                        .genre(movie.getGenre())
                        .showTm(movie.getShowTm())
                        .movieRating(movie.getMovieRating())
                        .releaseDate(movie.getReleaseDate())
                        .info(movie.getInfo()).build())
                .cinemaInfo(cinemaPlusListToCinemaDtoResonseList(cinemaPlusList)).build();
    }

    default List<CinemaDto.Response> cinemaPlusListToCinemaDtoResonseList(List<CinemaPlus> cinemaPlusList) {
        return cinemaPlusList.stream()
                .map(cinemaPlus -> cinemaPlusToCinemaDtoResponse(cinemaPlus))
                .collect(toList());
    }

    default CinemaDto.Response cinemaPlusToCinemaDtoResponse(CinemaPlus cinemaPlus) {
        return CinemaDto.Response.builder()
                .cinemaId(cinemaPlus.getCinemaId())
                .brand(cinemaPlus.getBrand())
                .name(cinemaPlus.getName())
                .address(cinemaPlus.getAddress())
                .y(cinemaPlus.getX())
                .x(cinemaPlus.getY())
                .screenInfoList(screenInfoListToCinemaDtoScreenInfoList(cinemaPlus.getScreenInfoList())).build();
    }

    default List<CinemaDto.ScreenInfo> screenInfoListToCinemaDtoScreenInfoList(List<CinemaPlus.ScreenInfo> screenInfoList) {
        return screenInfoList.stream()
                .map(screenInfo -> screenInfoToCinemaDtoScreenInfo(screenInfo))
                .collect(toList());
    }

    default CinemaDto.ScreenInfo screenInfoToCinemaDtoScreenInfo(CinemaPlus.ScreenInfo screenInfo) {
        return CinemaDto.ScreenInfo.builder()
                .screenInfo(screenInfo.getScreenInfo())
                .runningTimeList(runningTimeListToRunningTimeDtoReponseList(screenInfo.getRunningTimeList())).build();
    }

    default List<RunningTimeDto.Response> runningTimeListToRunningTimeDtoReponseList(List<RunningTime> runningTimeList) {
        return runningTimeList.stream()
                .map(runningTime -> runningTimeToRunningTimeDtoResponse(runningTime))
                .collect(toList());
    }

    default RunningTimeDto.Response runningTimeToRunningTimeDtoResponse(RunningTime runningTime) {
        return RunningTimeDto.Response.builder()
                .runningTimeId(runningTime.getRunningTimeId())
                .startTime(runningTime.getStartTime())
                .endTime(runningTime.getEndTime()).build();
    }
}
