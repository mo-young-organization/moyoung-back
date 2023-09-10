package Moyoung.Server.cinema.repository;

import Moyoung.Server.cinema.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findAllByBrand(String brand);

    @Query("SELECT c FROM Cinema c WHERE ST_DISTANCE_SPHERE(POINT(c.longitude, c.latitude), POINT(:longitude, :latitude)) <= :distance")
    List<Cinema> findCinemasWithinDistance(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("distance") double distance);
}
