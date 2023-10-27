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
    private Long cinemaId;
    private String region_1;
    private String region_2;
    private String code;
    private String name;
    private String address;
    private String brand;
    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private double x;
    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private double y;

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
