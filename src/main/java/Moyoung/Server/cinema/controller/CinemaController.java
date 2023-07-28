package Moyoung.Server.cinema.controller;

import Moyoung.Server.cinema.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;
}
