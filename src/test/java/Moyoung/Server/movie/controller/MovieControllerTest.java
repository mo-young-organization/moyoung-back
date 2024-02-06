package Moyoung.Server.movie.controller;

import Moyoung.Server.cinema.service.CinemaService;
import Moyoung.Server.helper.MovieControllerHelper;
import Moyoung.Server.helper.StubData;
import Moyoung.Server.movie.mapper.MovieMapper;
import Moyoung.Server.movie.service.MovieService;
import Moyoung.Server.runningtime.service.RunningTimeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
public class MovieControllerTest implements MovieControllerHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;
    @MockBean
    private CinemaService cinemaService;
    @MockBean
    private RunningTimeService runningTimeService;
    @MockBean
    private MovieMapper movieMapper;

    @Test
    @DisplayName("Movie Get Test")
    public void getMovieTest() throws Exception {
        // given
        String movieName = "영화 제목";
        String x = "126.905217202756";
        String y = "37.4863264672324";
        String distance = "3000";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("movieName", movieName);
        params.add("x", x);
        params.add("y", y);
        params.add("distance", distance);

        given(cinemaService.findCinemaList(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble())).willReturn(StubData.getCinemaList());

        given(runningTimeService.findDistinctRunningTimeWithMovies(Mockito.anyList(), Mockito.anyString())).willReturn(StubData.getDistinctedRunningTimeWithMovies());

        given(movieMapper.moviesToResponses(Mockito.anyMap())).willReturn(StubData.getMovieResponses());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(MOVIE_URL, params));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Movie Rank Get Test")
    public void getMovieRankTest() throws Exception {
        // given
        given(movieService.findMovieRankByDate(Mockito.any(LocalDate.class))).willReturn(StubData.getMovieRanks());

        given(movieMapper.moviesToRankResponses(Mockito.anyList(), Mockito.any(LocalDate.class))).willReturn(StubData.getMovieRankResponse());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(MOVIE_RANK_URL));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }
}
