package Moyoung.Server.runningtime.crawler;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class MegaResponse {
    @SerializedName("megaMap")
    private MegaMap megaMap;

    @Getter
    public static class MegaMap {
        @SerializedName("movieFormList")
        private MovieForm[] movieFormList;
    }

    @Getter
    public static class MovieForm {
        @SerializedName("brchNo")
        private String brchNo;
        @SerializedName("brchNm")
        private String brchNm;
        @SerializedName("movieNm")
        private String movieNm;
        @SerializedName("playSchdlNo")
        private String playSchdlNo;
        @SerializedName("playStartTime")
        private String playStartTime;
        @SerializedName("playEndTime")
        private String playEndTime;
    }
}