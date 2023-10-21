package Moyoung.Server.cinema.repository;

import Moyoung.Server.cinema.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findAllByBrand(String brand);

@Query("SELECT c FROM Cinema c WHERE ST_DISTANCE_SPHERE(POINT(c.y, c.x), POINT(:longitude, :latitude)) <= :distance " +
        "AND ((:mega = true AND c.brand = 'mega') OR (:lotte = true AND c.brand = 'lotte') OR (:cgv = true AND c.brand = 'cgv') OR (:mega = false AND :lotte = false AND :cgv = false))")
List<Cinema> findCinemasWithinDistanceAndFilter(@Param("latitude") double latitude,
                                                @Param("longitude") double longitude,
                                                @Param("distance") double distance,
                                                @Param("mega") boolean mega,
                                                @Param("lotte") boolean lotte,
                                                @Param("cgv") boolean cgv);
}
