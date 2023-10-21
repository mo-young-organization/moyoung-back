package Moyoung.Server.crawler.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Cinema_crawler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cinema_crawlerId;
    private String brand;
    private String region_1;
    private String name;
    private String code;
}
