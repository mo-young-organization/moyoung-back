package Moyoung.Server.crawler.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class RankResponse {
    @SerializedName("boxOfficeResult")
    private BoxOfficeResult boxOfficeResult;

    @Getter
    public static class BoxOfficeResult {
        @SerializedName("dailyBoxOfficeList")
        private DailyBoxOffice[] dailyBoxOfficeList;
    }

    @Getter
    public static class DailyBoxOffice {
        @SerializedName("rank")
        private String rank;
        @SerializedName("movieNm")
        private String movieNm;
    }
}
