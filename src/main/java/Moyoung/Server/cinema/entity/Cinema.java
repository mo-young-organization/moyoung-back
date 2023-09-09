package Moyoung.Server.cinema.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cinemaId;
    private String brand;
    private String code;
    private String name;
    private String region;
    private String info;
    private String address;
    // 위도
    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private double latitude;
    // 경도
    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private double longitude;
}
