package Moyoung.Server.cinema.service;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.entity.CinemaPlus;
import Moyoung.Server.cinema.repository.CinemaRepository;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.service.RunningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final RunningTimeService runningTimeService;

    public List<CinemaPlus> find(double latitude, double longitude, double distance, Movie movie, LocalDate date) {
        List<CinemaPlus> cinemaPlusList = new ArrayList<>();
        List<Cinema> cinemaList = cinemaRepository.findCinemasWithinDistance(latitude, longitude, distance);

        for (Cinema cinema : cinemaList) {
            CinemaPlus cinemaPlus = new CinemaPlus(
                    cinema.getCinemaId(),
                    cinema.getBrand(),
                    cinema.getName(),
                    cinema.getAddress(),
                    cinema.getLatitude(),
                    cinema.getLongitude()
            );

            List<CinemaPlus.ScreenInfo> screenInfoList = new ArrayList<>();

            List<RunningTime> runningTimeList = runningTimeService.find(cinema, movie, date);

            for (RunningTime runningTime : runningTimeList) {
                boolean found = false;

                String runningTimeScreenInfo = runningTime.getScreenInfo();

                for (CinemaPlus.ScreenInfo screenInfo : screenInfoList) {
                    if (screenInfo.getScreenInfo().equals(runningTimeScreenInfo)) {
                        screenInfo.getRunningTimeList().add(runningTime);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    CinemaPlus.ScreenInfo newScreenInfo = new CinemaPlus.ScreenInfo();
                    newScreenInfo.setScreenInfo(runningTimeScreenInfo);
                    newScreenInfo.setRunningTimeList(new ArrayList<>());
                    newScreenInfo.getRunningTimeList().add(runningTime);

                    screenInfoList.add(newScreenInfo);
                }
            }

            if (screenInfoList.size() != 0) {
                cinemaPlus.setScreenInfoList(screenInfoList);
                cinemaPlusList.add(cinemaPlus);
            }
        }

        return cinemaPlusList;
    }
}
