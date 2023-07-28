package Moyoung.Server.cinema.repository;

import Moyoung.Server.cinema.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
}
