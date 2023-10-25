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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final RunningTimeService runningTimeService;

    public List<CinemaPlus> find(double x, double y, double distance, boolean mega, boolean lotte, boolean cgv, Movie movie, LocalDate date) {
        List<CinemaPlus> cinemaPlusList = new ArrayList<>();

        Set<String> brands = new HashSet<>();
        if (cgv) brands.add("CGV");
        if (mega) brands.add("Mega");
        if (lotte) brands.add("Lotte");
        if (!mega && !lotte && !cgv) {
            brands.add("Mega");
            brands.add("Lotte");
            brands.add("cgv");
        }
        List<Cinema> cinemaList = cinemaRepository.findCinemasWithinDistanceAndFilter(x, y, distance, brands);

        for (Cinema cinema : cinemaList) {
            CinemaPlus cinemaPlus = new CinemaPlus(
                    cinema.getCinemaId(),
                    cinema.getBrand(),
                    cinema.getName(),
                    cinema.getAddress(),
                    cinema.getX(),
                    cinema.getY()
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

    public List<Cinema> findCinemaList(double x, double y, double distance) {
        return cinemaRepository.findCinemasByWithinDistance(x, y, distance);
    }
}
