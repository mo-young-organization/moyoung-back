package Moyoung.Server.cinema.entity;

import Moyoung.Server.runningtime.entity.RunningTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// 응답용 엔티티
@Getter
@Setter
@NoArgsConstructor
public class CinemaPlus {
    private Long cinemaId;
    private String region_1;
    private String region_2;
    private String code;
    private String name;
    private String address;
    private String brand;
    private double x;
    private double y;
    private List<ScreenInfo> screenInfoList;

    @Getter
    @Setter
    public static class ScreenInfo {
        private String screenInfo;
        private List<RunningTime> runningTimeList;
    }

    public CinemaPlus(long cinemaId, String brand, String name, String address, double latitude, double longitude) {
        this.cinemaId = cinemaId;
        this.brand = brand;
        this.name = name;
        this.address = address;
        this.x = latitude;
        this.y = longitude;
    }
}
