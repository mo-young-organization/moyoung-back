package Moyoung.Server.runningtime.crawler;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.repository.CinemaRepository;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.repository.MovieRepository;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.repository.RunningTimeRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

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
public class RunningTimeCrawlerService {
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final RunningTimeRepository runningTimeRepository;

    public void crawlMegaBox() throws IOException {
        try {
            List<Cinema> megaCinemaList = cinemaRepository.findAllByBrand("Mega");

            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();

            // 현재 날짜에서 5일을 더한 날짜 계산
            LocalDate playDate = currentDate.plus(5, ChronoUnit.DAYS);

            // 날짜 포맷 지정 (yyyyMMdd 형식으로)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String playDateStr = playDate.format(formatter);

            for (Cinema cinema : megaCinemaList) {
                CloseableHttpClient httpClient = HttpClients.createDefault();

                // URL 설정
                String url = "https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do";

                // POST 요청 생성
                HttpPost httpPost = new HttpPost(url);
                String cinemaNo = cinema.getCode();

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
                CloseableHttpResponse response = httpClient.execute(httpPost);

                // 응답 받기
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);

                // Gson을 사용하여 JSON 파싱
                Gson gson = new Gson();
                MegaResponse jsonResponse = gson.fromJson(responseString, MegaResponse.class);

                // 위도 경도 주소 설정
                if (cinema.getLatitude() == 0 && cinema.getLongitude() == 0) {
                    MegaResponse.BrchInfo brchInfo =  jsonResponse.getMegaMap().getBrchInfo();
                    cinema.setLatitude(brchInfo.getBrchLat());
                    cinema.setLongitude(brchInfo.getBrchLon());
                    String address = brchInfo.getAddress();

                    // 괄호 대체
                    address = address.replace("&#40;", "(");
                    address = address.replace("&#41;", ")");

                    cinema.setAddress(address);

                    cinemaRepository.save(cinema);
                }

                // RunningTime 객체 생성 및 저장
                for (MegaResponse.MovieForm movieForm : jsonResponse.getMegaMap().getMovieFormList()) {
                    RunningTime runningTime = new RunningTime();

                    String screenId = movieForm.getScreenId();

                    // 괄호 대체
                    screenId = screenId.replace("&#40;", "(");
                    screenId = screenId.replace("&#41;", ")");

                    runningTime.setScreenInfo(screenId);

                    // 조조 여부
                    if (movieForm.getPlayTyCdNm().equals("조조")) {
                        runningTime.setEarlyMorning(true);
                    }

                    String startTimeStr = movieForm.getPlayStartTime();
                    String endTimeStr = movieForm.getPlayEndTime();
                    LocalDate playStartDate = playDate;
                    LocalDate playEndDate = playDate;

                    int startHour = Integer.parseInt(startTimeStr.substring(0, 2));
                    int startMinute = Integer.parseInt(startTimeStr.substring(3, 5));

                    int endHour = Integer.parseInt(endTimeStr.substring(0, 2));
                    int endMinute = Integer.parseInt(endTimeStr.substring(3, 5));

                    if (startHour >= 24) {
                        playStartDate = playStartDate.plusDays(1);
                        startHour -= 24; // 24를 빼줌
                    }
                    if (endHour >= 24) {
                        playEndDate = playEndDate.plusDays(1);
                        endHour -= 24; // 24를 빼줌
                    }

                    LocalTime startTime = LocalTime.of(startHour, startMinute);
                    LocalTime endTime = LocalTime.of(endHour, endMinute);

                    runningTime.setStartTime(LocalDateTime.of(playStartDate, startTime));
                    runningTime.setEndTime(LocalDateTime.of(playEndDate, endTime));

                    runningTime.setCinema(cinema);
                    String movieName = movieForm.getMovieNm();

                    // 괄호 대체
                    movieName = movieName.replace("&#40;", "(");
                    movieName = movieName.replace("&#41;", ")");

                    Optional<Movie> optionalMovie = movieRepository.findByName(movieName);
                    Movie movie;
                    if (optionalMovie.isPresent()) {
                        movie = optionalMovie.get();
                    } else {
                        movie = new Movie();
                        movie.setName(movieName);

                        // 관람 등급 설정
                        movie.setMovieRating(movieForm.getAdmisClassCdNm());

                        String rpstMovieNo = movieForm.getRpstMovieNo();
                        String movieInfoUrl = "https://www.megabox.co.kr/movie-detail?rpstMovieNo=" + rpstMovieNo;

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

                        movieRepository.save(movie);
                    }
                    runningTime.setMovie(movie);

                    runningTimeRepository.save(runningTime);
                }

                // 클라이언트 종료
                httpClient.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crawlLotteCinema() {
        try {
            List<Cinema> megaCinemaList = cinemaRepository.findAllByBrand("Lotte");
            LocalDate currentDate = LocalDate.now();
            LocalDate playDate = currentDate.plus(4, ChronoUnit.DAYS);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String playDateStr = playDate.format(formatter);

            for (Cinema cinema : megaCinemaList) {
                System.out.println(cinema.getCinemaId());
                String region = cinema.getRegion();
                String regionNo = getRegionNumber(region);
                String cinemaNo = cinema.getCode();

                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    String uri = "https://www.lottecinema.co.kr/LCWS/Ticketing/TicketingData.aspx";
                    HttpPost httpPost = new HttpPost(uri);

                    List<NameValuePair> params = new ArrayList<>();
                    String paramList = createParamList(playDateStr, regionNo, cinemaNo);
                    params.add(new BasicNameValuePair("paramList", paramList));
                    httpPost.setEntity(new UrlEncodedFormEntity(params));

                    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                        HttpEntity responseEntity = response.getEntity();
                        String responseString = EntityUtils.toString(responseEntity);

                        Gson gson = new Gson();
                        LotteResponse lotteResponse = gson.fromJson(responseString, LotteResponse.class);

                        if (lotteResponse.getPlaySeqsHeader() != null) {
                            List<LotteResponse.MovieForm> movieFormList = lotteResponse.getPlaySeqsHeader().getMovieFormList();

                            if (movieFormList != null) {
                                for (LotteResponse.MovieForm movieForm : movieFormList) {
                                    if (movieForm == null) {
                                        continue;
                                    }
                                    RunningTime runningTime = new RunningTime();
                                    String startTimeStr = movieForm.getStartTime();
                                    String endTimeStr = movieForm.getEndTime();
                                    if (startTimeStr != null) {
                                        LocalDate playStartDate = playDate;
                                        LocalDate playEndDate = playDate;

                                        int startHour = Integer.parseInt(startTimeStr.substring(0, 2));
                                        int startMinute = Integer.parseInt(startTimeStr.substring(3, 5));

                                        int endHour = Integer.parseInt(endTimeStr.substring(0, 2));
                                        int endMinute = Integer.parseInt(endTimeStr.substring(3, 5));

                                        if (startHour >= 24) {
                                            playStartDate = playStartDate.plusDays(1);
                                            startHour -= 24; // 24를 빼줌
                                        }
                                        if (endHour >= 24) {
                                            playEndDate = playEndDate.plusDays(1);
                                            endHour -= 24; // 24를 빼줌
                                        }

                                        LocalTime startTime = LocalTime.of(startHour, startMinute);
                                        LocalTime endTime = LocalTime.of(endHour, endMinute);

                                        runningTime.setStartTime(LocalDateTime.of(playStartDate, startTime));
                                        runningTime.setEndTime(LocalDateTime.of(playEndDate, endTime));
                                    }
                                    String screenInfo = movieForm.getScreenId();
                                    String brandNm = movieForm.getBrandNm();
                                    if (brandNm != null && brandNm.equals("샤롯데")) {
                                        screenInfo = brandNm;
                                    } else if (screenInfo != null) {
                                        System.out.println(screenInfo);
                                        screenInfo = screenInfo.substring(4) + "관";
                                        if (screenInfo.charAt(0) == '0') {
                                            System.out.println(screenInfo);
                                            screenInfo = screenInfo.substring(1);
                                        }
                                    }

                                    runningTime.setScreenInfo(screenInfo);

                                    String movieName = movieForm.getMovieNameKR();
                                    Movie movie = getOrCreateMovie(movieName);
                                    runningTime.setMovie(movie);

                                    runningTimeRepository.save(runningTime);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRegionNumber(String region) {
        switch (region) {
            case "서울":
                return "1";
            case "경기/인천":
                return "2";
            case "충청/대전":
                return "3";
            case "전라/광주":
                return "4";
            case "경북/대구":
                return "5";
            case "경남/부산/울산":
                return "101";
            case "강원":
                return "6";
            case "제주":
                return "7";
            default:
                return null;
        }
    }

    private String createParamList(String playDateStr, String regionNo, String cinemaNo) {
        return "{\n" +
                "    \"MethodName\": \"GetPlaySequence\",\n" +
                "    \"channelType\": \"HO\",\n" +
                "    \"osType\": \"W\",\n" +
                "    \"osVersion\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36\",\n" +
                "    \"playDate\": \"" + playDateStr + "\",\n" +
                "    \"cinemaID\": \"1|" + regionNo + "|" + cinemaNo + "\",\n" +
                "    \"representationMovieCode\": \"\"\n" +
                "}";
    }

    private Movie getOrCreateMovie(String movieName) {
        Optional<Movie> optionalMovie = movieRepository.findByName(movieName);
        return optionalMovie.orElseGet(() -> {
            Movie movie = new Movie();
            movie.setName(movieName);
            return movieRepository.save(movie);
        });
    }
}
