package Moyoung.Server.movie.controller;

import Moyoung.Server.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
}
