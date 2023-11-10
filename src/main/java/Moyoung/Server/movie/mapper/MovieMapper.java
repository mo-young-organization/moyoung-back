package Moyoung.Server.movie.mapper;

import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    default List<MovieDto.Response> moviesToResponses(Map<String, List<RunningTime>> movies) {
        List<MovieDto.Response> responseList = new ArrayList<>();

        for (Map.Entry<String, List<RunningTime>> entry : movies.entrySet()) {
            String movieName = entry.getKey();
            List<RunningTime> runningTimes = entry.getValue();

            // MovieDto.Response 객체 생성
            MovieDto.Response response = new MovieDto.Response();
            Movie movie = runningTimes.get(0).getMovie();
            response.setName(movieName);
            response.setEnName(movie.getEnName());
            response.setThumbnailUrl(movie.getThumbnailUrl());
            response.setMovieInfo(movie.getInfo());
            response.setMovieRating(movie.getMovieRating());

            // MovieDto.Type 리스트 생성
            List<MovieDto.Type> typeList = new ArrayList<>();

            for (RunningTime runningTime : runningTimes) {
                Long movieId = runningTime.getMovie().getMovieId();
                String type = getType(runningTime.getMovie().getName(), movie.getCountry());

                // 해당 종류(type)가 이미 리스트에 있는지 확인
                Optional<MovieDto.Type> existingType = typeList.stream()
                        .filter(t -> t.getMovieId() == movieId && t.getType().equals(type))
                        .findFirst();

                if (existingType.isPresent()) {
                    // 이미 해당 종류(type)가 리스트에 있으면 count 증가
                    existingType.get().setCount(existingType.get().getCount() + 1);
                } else {
                    // 해당 종류(type)가 리스트에 없으면 새로 추가
                    typeList.add(new MovieDto.Type(movieId, type, 1));
                }
            }

            response.setTypeList(typeList);
            responseList.add(response);
        }

        return responseList;
    }

    // 괄호 및 괄호 내용 추출
    private static String getType(String name, String country) {
        name = name.replaceAll(".*?\\((.*?)\\)", "$1").trim();
        String returnName;

        if (name.contains("3D") || name.contains("4D")) {
            returnName = name.contains("ScreenX") ? "2D ScreenX" : "2D";
        } else {
            returnName = name;
        }

        if (!name.contains("더빙") && !country.contains("한국") && !country.contains("기타")) {
            returnName += "(자막)";
        }
        if (name.contains("더빙")) {
            returnName += "(더빙)";
        }

        return returnName;
    }


    default MovieDto.RankResponse moviesToRankResponses(List<MovieRank> movieRanks, LocalDate date) {
        return MovieDto.RankResponse.builder()
                .date(date)
                .ranks(movieRanks.stream().map(movieRank -> movieRankToRank(movieRank)).collect(Collectors.toList())).build();
    }

    default MovieDto.Rank movieRankToRank(MovieRank movieRank) {
        Movie movie = movieRank.getMovie();
        String movieName = movie.getName();
        movieName = movieName.replaceAll("\\(.*?\\)", "").trim();

        return MovieDto.Rank.builder()
                .rank(movieRank.getMovieRank())
                .name(movieName)
                .thumbnailUrl(movie.getThumbnailUrl()).build();
    }
}
