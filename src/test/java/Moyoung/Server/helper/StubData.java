package Moyoung.Server.helper;

import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.cinema.dto.CinemaDto;
import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.entity.CinemaPlus;
import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.runningtime.dto.RunningTimeDto;
import Moyoung.Server.runningtime.entity.RunningTime;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
        MemberDto.PostInfo info = new MemberDto.PostInfo();
        info.setDisplayName("test");
        info.setGender(Boolean.TRUE);
        info.setAge(1);

        return gson.toJson(info);
    }

    public static String getPatchInfoContent() {
        Gson gson = new Gson();
        MemberDto.PatchInfo info = new MemberDto.PatchInfo();
        info.setDisplayName("test");
        info.setGender(Boolean.TRUE);
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

    public static String getPostRecruitingArticleContent() {
        Gson gson = new Gson();
        RecruitingArticleDto.PostPatch content = new RecruitingArticleDto.PostPatch();
        content.setTitle("제목");
        content.setRunningTimeId(1L);
        content.setMaxNum(4);
        content.setGender(1);
        content.setAges(List.of(1 ,2));

        return gson.toJson(content);
    }

    public static RecruitingArticleDto.Response getRecruitingArticleResponse() {
        return RecruitingArticleDto.Response.builder()
                .recruitingArticleId(1L)
                .writerMemberId(1L)
                .writerDisplayName("작성자 닉네임")
                .writerAge("작성자 연령대")
                .writerGender("작성자 성별")
                .title("모집글 제목")
                .cinemaRegion("영화관 소재지")
                .cinemaName("영화관 이름")
                .cinemaBrand("영화관 브랜드")
                .movieThumbnailUrl("포스터 이미지 URL")
                .movieName("영화 제목")
                .movieRating("영화 관람 등급")
                .startTime(LocalDateTime.now())
                .screenInfo("영화 정보")
                .maxNum(3)
                .currentNum(2)
                .gender("참여 성별 조건")
                .ages(List.of("참여 나이 요건"))
                .userInfos(List.of(RecruitingArticleDto.UserInfo.builder()
                        .memberId(1L)
                        .gender("참여자 성별")
                        .displayName("참여자 닉네임")
                        .age("참여자 연령대").build())).build();
    }

    public static List<RecruitingArticleDto.ResponseForList> getRecruitingArticleResponses() {
        return List.of(RecruitingArticleDto.ResponseForList.builder()
                    .recruitingArticleId(2L)
                    .writerDisplayName("작성자 닉네임")
                    .writerAge("작성자 연령대")
                    .title("모집글 제목")
                    .cinemaRegion("영화관 소재지")
                    .cinemaName("영화관 이름")
                    .cinemaBrand("영화관 브랜드")
                    .movieThumbnailUrl("포스터 이미지 URL")
                    .movieName("영화 제목")
                    .movieRating("영화 관람 등급")
                    .startTime(LocalDateTime.now())
                    .screenInfo("영화 정보")
                    .maxNum(3)
                    .currentNum(2)
                    .gender("참여 성별 조건")
                    .ages(List.of("참여 나이 요건")).build(),
                RecruitingArticleDto.ResponseForList.builder()
                    .recruitingArticleId(1L)
                    .writerDisplayName("작성자 닉네임")
                    .writerAge("작성자 연령대")
                    .title("모집글 제목")
                    .cinemaRegion("영화관 소재지")
                    .cinemaName("영화관 이름")
                    .cinemaBrand("영화관 브랜드")
                    .movieThumbnailUrl("포스터 이미지 URL")
                    .movieName("영화 제목")
                    .movieRating("영화 관람 등급")
                    .startTime(LocalDateTime.now())
                    .screenInfo("영화 정보")
                    .maxNum(3)
                    .currentNum(2)
                    .gender("참여 성별 조건")
                    .ages(List.of("참여 나이 요건")).build());
    }

    public static Page<RecruitingArticle> getPageRecruitingArticle() {
        RecruitingArticle recruitingArticle1 = new RecruitingArticle();
        RecruitingArticle recruitingArticle2 = new RecruitingArticle();

        return new PageImpl<>(List.of(recruitingArticle1, recruitingArticle2),
                PageRequest.of(0, 10, Sort.by("recruitingArticleId").descending()), 2);
    }

    public static List<CinemaPlus> getCinemaPlusList() {
        return List.of(
                new CinemaPlus()
        );
    }

    public static CinemaDto.NearResponse getNearResponse() {
        return CinemaDto.NearResponse.builder()
                .movieInfo(MovieDto.NearResponse.builder()
                        .movieId(1L)
                        .name("영화 제목")
                        .enName("영화 영어 제목")
                        .thumbnailUrl("포스터 이미지 URL")
                        .genre("영화 장르")
                        .showTm("영화 상영 시간")
                        .movieRating("영화 관람 등급")
                        .releaseDate("영화 개봉일")
                        .info("영화 정보").build())
                .cinemaInfo(List.of(CinemaDto.Response.builder()
                        .cinemaId(1L)
                        .brand("영화관 브랜드")
                        .name("영화관 이름")
                        .address("영화관 주소")
                        .x(126.905217202756)
                        .y(37.4863264672324)
                        .screenInfoList(List.of(CinemaDto.ScreenInfo.builder()
                                .screenInfo("상영관 정보")
                                .runningTimeList(List.of(RunningTimeDto.Response.builder()
                                        .runningTimeId(1L)
                                        .startTime(LocalDateTime.now())
                                        .endTime(LocalDateTime.now()).build())
                                ).build()))
                        .build()))
                .build();
    }
}
