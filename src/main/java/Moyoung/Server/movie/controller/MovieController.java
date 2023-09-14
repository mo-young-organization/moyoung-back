package Moyoung.Server.movie.controller;

import Moyoung.Server.dto.MultiResponseDto;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.mapper.MovieMapper;
import Moyoung.Server.movie.service.MovieService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping("/movie")
    public ResponseEntity getMovieByMovieName(@RequestParam String movieName,
                                              @RequestParam(required = false) Integer page) {

        if (StringUtils.isBlank(movieName)) {
            return new ResponseEntity("movieName 파라미터는 비어 있을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Page<Movie> moviePage = movieService.findMoviesByName(movieName, page);
        List<Movie> movies = moviePage.getContent();

        return new ResponseEntity(new MultiResponseDto<>(movieMapper.moviesToResponses(movies), moviePage), HttpStatus.OK);
    }
}
