package Moyoung.Server.crawler.service;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.repository.CinemaRepository;
import Moyoung.Server.crawler.entity.Cinema_crawler;
import Moyoung.Server.crawler.repository.Cinema_crawlerRepository;
import Moyoung.Server.crawler.response.MegaResponse;
import Moyoung.Server.crawler.response.MovieInfoResultResponse;
import Moyoung.Server.crawler.response.TheaterSchedule;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.repository.MovieRepository;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
public class CrawlerServiceV2 {

    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final RunningTimeRepository runningTimeRepository;
    private final Cinema_crawlerRepository cinemaCrawlerRepository;

    @Value("${crawler.key}")
    private String KEY;

    @Value("${crawler.kakao.key}")
    private String KAKAOKEY;



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
            TheaterSchedule theaterSchedule = gson.fromJson(responseString, TheaterSchedule.class);

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


                    Movie movie;
                    Optional<Movie> optionalMovie = movieRepository.findByName(movieNm);
                    if (optionalMovie.isPresent()) {
                        movie = optionalMovie.get();
                    } else {
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

                        String openDt = movieInfo.getOpenDt();
                        List<MovieInfoResultResponse.Nation> nations = movieInfo.getNations();
                        List<MovieInfoResultResponse.Genre> genres = movieInfo.getGenres();
                        List<MovieInfoResultResponse.Audit> audits = movieInfo.getAudits();

                        movie = new Movie();
                        movie.setName(movieNm);
                        // 개봉일 추가
                        movie.setReleaseDate(openDt);

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

                        // 메가박스 크롤링을 통한 영화 포스터 url, 영화 소개 등 불러오기
                        List<Cinema_crawler> megaCinemaList = cinemaCrawlerRepository.findAll();

                        for (Cinema_crawler cinemaCrawler : megaCinemaList) {
                            httpClient = HttpClients.createDefault();

                            // URL 설정
                            url = "https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do";

                            // POST 요청 생성
                            httpPost = new HttpPost(url);
                            cinemaNo = cinemaCrawler.getCode();

                            String requestBody = "{\n" +
                                    "    \"masterType\": \"brch\",\n" +
                                    "    \"detailType\": \"area\",\n" +
                                    "    \"brchNo\": \"" + cinemaNo + "\",\n" +
                                    "    \"brchNo1\": \"" + cinemaNo + "\",\n" +
                                    "    \"firstAt\": \"Y\",\n" +
                                    "    \"playDe\": \"" + playDateStr + "\"\n" +
                                    "}";

                            StringEntity entity = new StringEntity(requestBody);
                            httpPost.setEntity(entity);

                            // 요청 헤더 설정
                            httpPost.setHeader("Content-Type", "application/json");
                            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36");

                            // 요청 보내기
                            response = httpClient.execute(httpPost);

                            // 응답 받기
                            responseEntity = response.getEntity();
                            responseString = EntityUtils.toString(responseEntity);

                            // Gson을 사용하여 JSON 파싱
                            gson = new Gson();
                            MegaResponse jsonResponse = gson.fromJson(responseString, MegaResponse.class);

                            for (MegaResponse.MovieForm movieForm : jsonResponse.getMegaMap().getMovieFormList()) {

                                // 괄호와 괄호 내용 제거
                                // 영화 제목이 포함되지 않았다면 다음 movieForm 실행
                                if (!movieForm.getMovieNm().contains(removeParentheses(movieNm))) continue;

                                String rpstMovieNo = movieForm.getRpstMovieNo();
                                movieInfoUrl = "https://www.megabox.co.kr/movie-detail?rpstMovieNo=" + rpstMovieNo;

                                try {
                                    // Jsoup을 사용하여 웹 페이지를 가져옴
                                    Document doc = Jsoup.connect(movieInfoUrl).get();

                                    // 포스터 이미지를 포함하는 HTML 요소를 선택
                                    Element posterElement = doc.selectFirst("#contents > div.movie-detail-page > div.movie-detail-cont > div.poster > div > img");

                                    if (posterElement != null) {
                                        // 포스터 이미지의 src 속성을 가져옴
                                        String posterImageUrl = posterElement.attr("src");
                                        movie.setThumbnailUrl(posterImageUrl);
                                    } else {
                                        System.out.println("포스터 이미지를 찾을 수 없습니다.");
                                    }

                                    // 영화 정보 텍스트를 가져옴
                                    Element descriptionMeta = doc.select("meta[property=description]").first();

                                    if (descriptionMeta != null) {
                                        String description = descriptionMeta.attr("content");
                                        movie.setInfo(description);
                                    } else {
                                        System.out.println("영화 정보를 찾을 수 없습니다.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                outerLoop: break outerLoop;
                            }
                        }
                    }
                    movie = movieRepository.save(movie);
                    runningTime.setMovie(movie);
                    runningTimeRepository.save(runningTime);
                }
            }
        }
    }


    // 영화 뒤의 괄호와 괄호 안 내용 제거
    private static String removeParentheses(String input) {
        int startIndex = input.indexOf('(');
        int endIndex = input.indexOf(')');

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return input.substring(0, startIndex).trim() + input.substring(endIndex + 1);
        }

        return input;
    }

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
