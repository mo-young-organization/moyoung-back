package Moyoung.Server.movie.service;

import Moyoung.Server.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
    private MovieRepository movieRepository;
}
