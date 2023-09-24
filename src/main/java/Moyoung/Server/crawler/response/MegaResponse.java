package Moyoung.Server.crawler.response;

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
        @SerializedName("brchInfo")
        private BrchInfo brchInfo;
    }

    @Getter
    public static class BrchInfo {
        @SerializedName("roadNmAddr")
        private String address;
        @SerializedName("brchLat")
        private double brchLat;
        @SerializedName("brchLon")
        private double brchLon;
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
        @SerializedName("theabExpoNm")
        private String screenId;
        @SerializedName("playTyCdNm")
        private String playTyCdNm;
        @SerializedName("admisClassCdNm")
        private String admisClassCdNm;
        @SerializedName("rpstMovieNo")
        private String rpstMovieNo;
    }
}