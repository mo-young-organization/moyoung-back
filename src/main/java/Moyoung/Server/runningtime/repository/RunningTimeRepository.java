package Moyoung.Server.runningtime.repository;

import Moyoung.Server.runningtime.entity.RunningTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningTimeRepository extends JpaRepository<RunningTime, Long> {
}
