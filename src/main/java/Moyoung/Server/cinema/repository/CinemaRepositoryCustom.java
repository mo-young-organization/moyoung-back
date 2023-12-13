package Moyoung.Server.cinema.repository;

import Moyoung.Server.cinema.entity.Cinema;

import java.util.List;
import java.util.Set;

public interface CinemaRepositoryCustom {
    List<Cinema> findCinemasWithinDistanceAndFilter(double longitude,
                                                    double latitude,
                                                    double distance,
                                                    Set<String> brands);

    List<Cinema> findCinemasByWithinDistance(double x,
                                             double y,
                                             double distance);
}
