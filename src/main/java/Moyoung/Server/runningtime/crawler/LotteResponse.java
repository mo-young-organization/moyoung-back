package Moyoung.Server.runningtime.crawler;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

public class LotteResponse {
    @Getter
    public static class MovieForm {
        @SerializedName("MovieNameKR")
        private String movieNameKR;

        @SerializedName("FilmNameKR")
        private String filmNameKR;

        @SerializedName("StartTime")
        private String startTime;

        @SerializedName("EndTime")
        private String endTime;

        @SerializedName("ScreenID")
        private String ScreenId;

        @SerializedName("BrandNm_KR")
        private String BrandNm;
    }

    @Getter
    public static class PlaySeqsHeader {
        @SerializedName("Items")
        private List<MovieForm> movieFormList;
    }

    @Getter
    @SerializedName("PlaySeqsHeader")
    private PlaySeqsHeader playSeqsHeader;
}
