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
    private long cinemaId;
    private String brand;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
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
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
