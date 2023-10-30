package Moyoung.Server.helper;

import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.runningtime.entity.RunningTime;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class StubData {
    public static class MockSecurity {
        public static String getValidAccessToken(String secretKey) {
            JwtTokenizer jwtTokenizer = new JwtTokenizer();
            Map<String, Object> claims = new HashMap<>();
            claims.put("memberId", 1L);

            String subject = "test access token";
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
            Date expiration = calendar.getTime();

            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);

            String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

            return accessToken;
        }
    }

    public static String getPostMemberContent() {
        Gson gson = new Gson();
        MemberDto.Post post = new MemberDto.Post();
        post.setId("test");
        post.setPassword("1234");

        return gson.toJson(post);
    }

    public static String getPostInfoContent() {
        Gson gson = new Gson();
        MemberDto.Info info = new MemberDto.Info();
        info.setDisplayName("test");
        info.setGender(true);
        info.setAge(1);

        return gson.toJson(info);
    }

    public static String getCheckDisplayNameContent() {
        Gson gson = new Gson();
        MemberDto.DisplayName displayName = new MemberDto.DisplayName();
        displayName.setDisplayName("test");

        return gson.toJson(displayName);
    }

    public static List<Cinema> getCinemaList() {
        Cinema cinema = new Cinema();
        cinema.setCinemaId(1L);
        cinema.setRegion_1("지역");
        cinema.setCode("크롤링 코드");
        cinema.setAddress("영화관 주소");
        cinema.setBrand("영화 브랜드");
        cinema.setXY(126.905217202756, 37.4863264672324);

        return List.of(
                cinema
        );
    }

    public static Map<String, List<RunningTime>> getDistinctedRunningTimeWithMovies() {
        LocalDateTime localDateTime = LocalDateTime.now();
        RunningTime runningTime = new RunningTime();
        runningTime.setRunningTimeId(1L);
        runningTime.setStartTime(localDateTime);
        runningTime.setEndTime(localDateTime.plusHours(1));
        runningTime.setScreenInfo("상영관 정보");

        Map<String, List<RunningTime>> map = new HashMap<>();
        map.put("영화제목", List.of(runningTime));

        return map;
    }

    public static List<MovieDto.Response> getMovieResponses() {
        MovieDto.Response response = new MovieDto.Response();
        MovieDto.Type type = new MovieDto.Type(1L, "(상영 방식)", 1);
        response.setName("영화 제목");
        response.setEnName("English Movie Name");
        response.setMovieRating("영화 관람 등급");
        response.setThumbnailUrl("포스터 url");
        response.setMovieInfo("영화 정보");
        response.setTypeList(
                List.of(type)
        );

        return List.of(
                response
        );
    }

    public static List<MovieRank> getMovieRanks() {
        MovieRank movieRank = new MovieRank();
        movieRank.setMovieRankId(1L);
        movieRank.setMovieRank(1);
        movieRank.setDate(LocalDate.now());

        return List.of(
                movieRank
        );
    }

    public static MovieDto.RankResponse getMovieRankResponse() {
        return MovieDto.RankResponse.builder()
                .date(LocalDate.now())
                .ranks(
                        List.of(MovieDto.Rank.builder().rank(1).name("영화제목").thumbnailUrl("포스터 url").build())
                ).build();
    }
}
