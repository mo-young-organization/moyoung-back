package Moyoung.Server.cinema.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cinemaId;
    private String region;
    private double dx;
    private double dy;

    @Enumerated
    private Type type;

    public enum Type {
        CGV("CGV"),
        MEGABOX("메가박스"),
        LOTTECINEMA("롯데시네마");

        @Getter
        private String name;
        Type(String name) {
            this.name = name;
        }
    }
}
