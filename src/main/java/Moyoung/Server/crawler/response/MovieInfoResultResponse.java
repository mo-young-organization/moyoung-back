package Moyoung.Server.crawler.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieInfoResultResponse {
    @SerializedName("movieInfoResult")
    private MovieInfoResult movieInfoResult;

    @Getter
    public static class MovieInfoResult {
        @SerializedName("movieInfo")
        private MovieInfo movieInfo;
    }


    @Getter
    public static class MovieInfo {
        @SerializedName("showTm")
        private String showTm;
        @SerializedName("openDt")
        private String openDt;
        @SerializedName("nations")
        private List<Nation> nations;
        @SerializedName("genres")
        private List<Genre> genres;
        @SerializedName("audits")
        private List<Audit> audits;
        @SerializedName("showTypes")
        private List<ShowType> showTypes;
    }

    @Getter
    public static class Nation {
        @SerializedName("nationNm")
        private String nationNm;
    }

    @Getter
    public static class Genre {
        @SerializedName("genreNm")
        private String genreNm;
    }

    @Getter
    public static class Audit {
        @SerializedName("watchGradeNm")
        private String watchGradeNm;
    }

    @Getter
    public static class ShowType {
        @SerializedName("showTypeGroupNm")
        private String showTypeGroupNm;
        @SerializedName("showTypeNm")
        private String showTypeNm;
    }
}
