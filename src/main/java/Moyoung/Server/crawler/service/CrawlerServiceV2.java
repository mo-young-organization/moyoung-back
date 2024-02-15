package Moyoung.Server.crawler.service;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.repository.CinemaRepository;
import Moyoung.Server.crawler.response.MovieInfoResultResponse;
import Moyoung.Server.crawler.response.RankResponse;
import Moyoung.Server.crawler.response.TheaterSchedule;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.movie.repository.MovieRankRepository;
import Moyoung.Server.movie.repository.MovieRepository;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlerServiceV2 {

    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final RunningTimeRepository runningTimeRepository;
    private final MovieRankRepository movieRankRepository;


    @Value("${crawler.key}")
    private String KEY;

    @Value("${crawler.kakao.key}")
    private String KAKAOKEY;

    @Value("${crawler.image}")
    private String IMAGE_URL;

    // 영화 순위 크롤링 메서드 (1위 부터 10위까지)
    // 0시 0분 5초에 받아오려 했으나 데이터 갱신되려면 시간이 조금 필요한 듯 하다
    @Scheduled(cron = "0 0 12 * * *") // 매일 12시 0분 0초 실행
    public void crawlMovieRank() throws IOException {
        // 당일 날짜는 정보가 없기 때문에 전일로 진행
        LocalDate date = LocalDate.now().minusDays(1);

        // 날짜 포맷 지정 (yyyyMMdd 형식으로)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = date.format(formatter);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();



            String url = UriComponentsBuilder.fromUriString("http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
                    .queryParam("key", KEY)
                    .queryParam("targetDt", dateStr)
                    .queryParam("itemPerPage", 10).build().toString();

            // Get 요청 생성
            HttpGet httpGet = new HttpGet(url);

            // 요청 보내기
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 응답 받기
            org.apache.http.HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            // Gson을 사용하여 JSON 파싱
            Gson gson = new Gson();
            RankResponse jsonResponse = gson.fromJson(responseString, RankResponse.class);

            if (jsonResponse != null) {
                RankResponse.DailyBoxOffice[] dailyBoxOffices = jsonResponse.getBoxOfficeResult().getDailyBoxOfficeList();

                for (int i = 0; i < dailyBoxOffices.length; i++) {
                    MovieRank movieRank = new MovieRank();
                    movieRank.setDate(date);
                    movieRank.setMovieRank(i + 1);

                    RankResponse.DailyBoxOffice dailyBoxOffice = dailyBoxOffices[i];
                    String movieNm = dailyBoxOffice.getMovieNm();

                    Movie movie = new Movie();

                    try {
                        movie = movieRepository.findAllByNameContains(movieNm).get(0);
                    } catch (IndexOutOfBoundsException e) { // 영화 정보가 없을 경우
                        movie = crawlMovieInfo(httpClient, gson, movieNm, dailyBoxOffice.getMovieCd(), movie);
                        movie = movieRepository.save(movie);
                    }

                    movieRank.setMovie(movie);

                    movieRankRepository.save(movieRank);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 영화 상영시간 크롤링 메서드
    @Scheduled(cron = "5 0 0 * * *") // 매일 0시 5분 0초 실행
    public void crawlRunningTime() throws IOException {
        List<Cinema> cinemaList = cinemaRepository.findAll();

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜에서 5일을 더한 날짜 계산
        LocalDate playDate = currentDate.plus(5, ChronoUnit.DAYS);

        // 날짜 포맷 지정 (yyyyMMdd 형식으로)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String playDateStr = playDate.format(formatter);

        for (Cinema cinema : cinemaList ) {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // URL 설정
            String url = "https://www.kobis.or.kr/kobis/business/mast/thea/findSchedule.do";

            // POST 요청 생성
            HttpPost httpPost = new HttpPost(url);
            String cinemaNo = cinema.getCode();

            // 쿼리 파람 추가
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("theaCd", cinemaNo));
            params.add(new BasicNameValuePair("showDt", playDateStr));

            // POST 요청 엔티티에 설정
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // 요청 보내기
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // 응답 받기
            org.apache.http.HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            // Gson을 사용하여 JSON 파싱
            Gson gson = new Gson();
            TheaterSchedule theaterSchedule;
            try {
                theaterSchedule = gson.fromJson(responseString, TheaterSchedule.class);
            } catch (Exception e) {
                log.error("Json Cinema:{}", cinema.getCinemaId());
                continue;
            }

            // RunningTime에 필요한 정보 추출
            List<TheaterSchedule.MovieSchedule> schedules = theaterSchedule.getSchedule();
            for (TheaterSchedule.MovieSchedule schedule : schedules) {
                String scrnNm = schedule.getScrnNm();
                String movieNm = schedule.getMovieNm();
                String showTm = schedule.getShowTm();
                String movieCd = schedule.getMovieCd();

                String[] showTmArr = showTm.split(",");

                for (String startTm : showTmArr) {
                    RunningTime runningTime = new RunningTime();

                    int startHour = Integer.parseInt(startTm.substring(0, 2));
                    int startMinute = Integer.parseInt(startTm.substring(3));

                    LocalTime startTime;

                    if (startHour >= 24) {
                        int tomorrowStartHour = startHour - 24;
                        startTime = LocalTime.of(tomorrowStartHour, startMinute);
                    } else {
                        startTime = LocalTime.of(startHour, startMinute);
                    }

                    runningTime.setStartTime(LocalDateTime.of(playDate, startTime));
                    runningTime.setCinema(cinema);
                    runningTime.setScreenInfo(scrnNm);


                    Movie movie = new Movie();
                    Optional<Movie> optionalMovie = movieRepository.findByName(movieNm);
                    if (optionalMovie.isPresent()) {
                        movie = optionalMovie.get();

                        // 영화 객체에 빠진 부분이 없으면 다시 크롤링
                        // 다시 크롤링 해도 없는 경우가 있으므로 금일 크롤링을 진행 했다면 더이상 안하게 함
                        movie = reCrawlMovieInfo(playDate, httpClient, gson, movieNm, movieCd, movie);
                    } else {
                        movie = crawlMovieInfo(httpClient, gson, movieNm, movieCd, movie);

                    }
                    movie.setLastAddedAt(playDate);
                    movie = movieRepository.save(movie);
                    runningTime.setMovie(movie);
                    showTm = movie.getShowTm();
                    try {
                        if (showTm != null && !showTm.isEmpty()) {
                            runningTime.setEndTime(runningTime.getStartTime().plusMinutes(Long.parseLong(showTm) + 10L));
                        }
                    } catch (NumberFormatException e) {
                        log.error("Cinema: {} Movie: {}", cinema.getName(), movie.getName());
                    }
                    runningTimeRepository.save(runningTime);
                }
            }
        }
    }

    // 상영시간 당일 누락분 크롤링 메서드
    @Scheduled(cron = "0 10 0 * * *") // 매일 0시 10분 0초 실행
    public void reCrawlTodayRunningTime() throws IOException {
        List<Cinema> cinemaList = cinemaRepository.findAll();

        // 현재 날짜 가져오기
        LocalDate playDate = LocalDate.now();

        // 날짜 포맷 지정 (yyyyMMdd 형식으로)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String playDateStr = playDate.format(formatter);

        for (Cinema cinema : cinemaList ) {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // URL 설정
            String url = "https://www.kobis.or.kr/kobis/business/mast/thea/findSchedule.do";

            // POST 요청 생성
            HttpPost httpPost = new HttpPost(url);
            String cinemaNo = cinema.getCode();

            // 쿼리 파람 추가
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("theaCd", cinemaNo));
            params.add(new BasicNameValuePair("showDt", playDateStr));

            // POST 요청 엔티티에 설정
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // 요청 보내기
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // 응답 받기
            org.apache.http.HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            // Gson을 사용하여 JSON 파싱
            Gson gson = new Gson();
            TheaterSchedule theaterSchedule;
            try {
                theaterSchedule = gson.fromJson(responseString, TheaterSchedule.class);
            } catch (Exception e) {
                log.error("Json Cinema:{}", cinema.getCinemaId());
                continue;
            }

            // RunningTime에 필요한 정보 추출
            List<TheaterSchedule.MovieSchedule> schedules = theaterSchedule.getSchedule();
            for (TheaterSchedule.MovieSchedule schedule : schedules) {
                String scrnNm = schedule.getScrnNm();
                String movieNm = schedule.getMovieNm();
                String showTm = schedule.getShowTm();
                String movieCd = schedule.getMovieCd();

                String[] showTmArr = showTm.split(",");

                for (String startTm : showTmArr) {
                    RunningTime runningTime = new RunningTime();

                    int startHour = Integer.parseInt(startTm.substring(0, 2));
                    int startMinute = Integer.parseInt(startTm.substring(3));

                    LocalTime startTime;

                    if (startHour >= 24) {
                        int tomorrowStartHour = startHour - 24;
                        startTime = LocalTime.of(tomorrowStartHour, startMinute);
                    } else {
                        startTime = LocalTime.of(startHour, startMinute);
                    }

                    runningTime.setStartTime(LocalDateTime.of(playDate, startTime));
                    runningTime.setCinema(cinema);
                    runningTime.setScreenInfo(scrnNm);

                    if (!runningTimeRepository.findRunningTimeCinemaAndStartTimeAndScreenInfo(cinema, runningTime.getStartTime(), scrnNm).isPresent()) {
                        Movie movie = new Movie();
                        Optional<Movie> optionalMovie = movieRepository.findByName(movieNm);
                        if (optionalMovie.isPresent()) {
                            movie = optionalMovie.get();

                            // 영화 객체에 빠진 부분이 없으면 다시 크롤링
                            // 다시 크롤링 해도 없는 경우가 있으므로 금일 크롤링을 진행 했다면 더이상 안하게 함
                            movie = reCrawlMovieInfo(playDate, httpClient, gson, movieNm, movieCd, movie);
                        } else {
                            movie = crawlMovieInfo(httpClient, gson, movieNm, movieCd, movie);

                        }
                        movie.setLastAddedAt(playDate);
                        movie = movieRepository.save(movie);
                        runningTime.setMovie(movie);
                        showTm = movie.getShowTm();
                        try {
                            if (showTm != null && !showTm.isEmpty()) {
                                runningTime.setEndTime(runningTime.getStartTime().plusMinutes(Long.parseLong(showTm) + 10L));
                            }
                        } catch (NumberFormatException e) {
                            log.error("Cinema: {} Movie: {}", cinema.getName(), movie.getName());
                        }
                        runningTimeRepository.save(runningTime);
                    }
                }
            }
        }
    }


    private Movie reCrawlMovieInfo(LocalDate playDate, CloseableHttpClient httpClient, Gson gson, String movieNm, String movieCd, Movie movie) throws IOException {
        if (!movie.getLastAddedAt().equals(playDate)) {
            log.info("MovieReload: {}", movie.getName());
            movie.setGenre(null);
            movie.setCountry(null);
            movie = crawlMovieInfo(httpClient, gson, movieNm, movieCd, movie);
        }
        return movie;
    }

    @NotNull
    private Movie crawlMovieInfo(CloseableHttpClient httpClient, Gson gson, String movieNm, String movieCd, Movie movie) throws IOException {
        HttpPost httpPost;
        CloseableHttpResponse response;
        String url;
        String movieInfoUrl = UriComponentsBuilder.fromUriString("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json")
                .queryParam("key", KEY)
                .queryParam("movieCd", movieCd).build().toString();

        HttpGet httpGet = new HttpGet(movieInfoUrl);

        // 요청 보내기
        CloseableHttpResponse movieResponse = httpClient.execute(httpGet);

        // 응답 받기
        org.apache.http.HttpEntity movieResponseEntity = movieResponse.getEntity();
        String movieResponseString = EntityUtils.toString(movieResponseEntity);

        // Gson을 사용하여 JSON 파싱
        MovieInfoResultResponse movieInfoResultResponse = gson.fromJson(movieResponseString, MovieInfoResultResponse.class);

        MovieInfoResultResponse.MovieInfo movieInfo = movieInfoResultResponse.getMovieInfoResult().getMovieInfo();

        String movieShowTm = movieInfo.getShowTm();
        String openDt = movieInfo.getOpenDt();
        String movieNmEn = movieInfo.getMovieNmEn();
        List<MovieInfoResultResponse.Nation> nations = movieInfo.getNations();
        List<MovieInfoResultResponse.Genre> genres = movieInfo.getGenres();
        List<MovieInfoResultResponse.Audit> audits = movieInfo.getAudits();

        movie.setName(movieNm);
        movie.setEnName(movieNmEn);
        // 개봉일 추가
        movie.setShowTm(movieShowTm);
        movie.setReleaseDate(openDt);
        movie.setMovieCode(movieCd);

        // 국가 추가
        for (MovieInfoResultResponse.Nation nation : nations) {
            movie.addCountry(nation.getNationNm());
        }
        // 장르 추가
        for (MovieInfoResultResponse.Genre genre : genres) {
            movie.addGenre(genre.getGenreNm());
        }
        // 관람 제한 연령 추가
        for (MovieInfoResultResponse.Audit audit : audits) {
            movie.setMovieRating(audit.getWatchGradeNm());
        }

        url = "https://www.kobis.or.kr/kobis/business/mast/mvie/searchMovieDtl.do";
        httpPost = new HttpPost(url);

        // form-data 생성
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("code", movieCd));
        formParams.add(new BasicNameValuePair("titleYN", "Y"));

        // UrlEncodedFormEntity로 form-data 설정
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
        httpPost.setEntity(formEntity);

        // 요청 보내기
        response = httpClient.execute(httpPost);

        // 응답을 파싱하고 Document로 변환
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        Document doc = Jsoup.parse(responseBody);

        Element imgElement = doc.select("a.fl.thumb img").first();

        String srcValue = null;
        if (imgElement != null) {
            // "src" 속성의 값을 가져옴
            srcValue = imgElement.attr("src");
            log.info("src 값: " + srcValue);
        } else {
            log.error("class 'fl thumb'를 가진 <a> 요소 내의 <img> 태그를 찾을 수 없습니다.");
        }

        movie.setThumbnailUrl(IMAGE_URL + srcValue);

        Element pElement = doc.select("p.desc_info").first();
        String pElementText = null;

        if (pElement != null) {
            pElementText = pElement.text();
        }

        movie.setInfo(pElementText);
        return movie;
    }

    // kakao Api를 통한 x, y(위도, 경도) 설정 메서드
    public void crawlCinemaXY() {
        List<Cinema> cinemaList = cinemaRepository.findAll();

        for (Cinema cinema : cinemaList) {
            String uri = "https://dapi.kakao.com/v2/local/search/address.json";

            RestTemplate restTemplate = new RestTemplate();
            String apiKey = "KakaoAK " + KAKAOKEY;
            String address = cinema.getAddress();

            // 요청 헤더에 만들기, Authorization 헤더 설정하기
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", apiKey);
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            UriComponents uriComponents = UriComponentsBuilder
                    .fromHttpUrl(uri)
                    .queryParam("query",address)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity, String.class);

            // API Response로부터 body 가져오기
            String body = response.getBody();
            JSONObject json = new JSONObject(body);
            // body에서 좌표 가져오기
            JSONArray documents = json.getJSONArray("documents");
            double x = documents.getJSONObject(0).getDouble("x");
            double y = documents.getJSONObject(0).getDouble("y");

            cinema.setXY(x, y);

            cinemaRepository.save(cinema);
        }
    }
}
