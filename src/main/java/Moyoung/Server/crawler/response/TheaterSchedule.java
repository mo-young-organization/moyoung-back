package Moyoung.Server.crawler.response;

import lombok.Getter;

import java.util.List;

@Getter
public class TheaterSchedule {
    private List<MovieSchedule> schedule;

    @Getter
    public static class MovieSchedule {
        private String scrnNm;
        private String movieNm;
        private String movieCd;
        private String showTm;
    }
}

