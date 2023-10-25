package Moyoung.Server.cinema.repository;

import Moyoung.Server.cinema.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    @Query("SELECT c FROM Cinema c WHERE ST_DISTANCE_SPHERE(POINT(c.x, c.y), POINT(:x, :y)) <= :distance " +
            "AND (c.brand IN :brands) " +
            "ORDER BY ST_DISTANCE_SPHERE(POINT(c.x, c.y), POINT(:x, :y))")
    List<Cinema> findCinemasWithinDistanceAndFilter(@Param("x") double longitude,
                                                    @Param("y") double latitude,
                                                    @Param("distance") double distance,
                                                    @Param("brands") Set<String> brands);

    @Query("SELECT c FROM Cinema c WHERE ST_DISTANCE_SPHERE(POINT(c.x, c.y), POINT(:x, :y)) <= :distance")
    List<Cinema> findCinemasByWithinDistance(@Param("x") double x,
                                             @Param("y") double y,
                                             @Param("distance") double distance);
}
