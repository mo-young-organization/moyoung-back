package Moyoung.Server.crawler.service;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerServiceV2 {

    @Value("${crawler.key}")
    private String KEY;

    @Value("${crawler.kakao.key}")
    private String KAKAOKEY;

    private final CinemaRepository cinemaRepository;

//    public void crawlRunningTime() {
//        List<Cinema> cinemaList = cinemaRepository.findAll();
//
//        // 현재 날짜 가져오기
//        LocalDate currentDate = LocalDate.now();
//
//        // 현재 날짜에서 5일을 더한 날짜 계산
//        LocalDate playDate = currentDate.plus(5, ChronoUnit.DAYS);
//
//        // 날짜 포맷 지정 (yyyyMMdd 형식으로)
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String playDateStr = playDate.format(formatter);
//
//        for (Cinema cinema : cinemaList ) {
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//
//            // URL 설정
//            String url = "https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do";
//
//            // POST 요청 생성
//            HttpPost httpPost = new HttpPost(url);
//            String cinemaNo = cinema.getCode();
//        }
//    }

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
