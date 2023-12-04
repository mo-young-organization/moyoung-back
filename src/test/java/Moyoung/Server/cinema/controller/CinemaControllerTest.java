package Moyoung.Server.cinema.controller;

import Moyoung.Server.cinema.mapper.CinemaMapper;
import Moyoung.Server.cinema.service.CinemaService;
import Moyoung.Server.helper.CinemaControllerHelper;
import Moyoung.Server.helper.StubData;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.service.MovieService;
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

@WebMvcTest(CinemaController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
public class CinemaControllerTest implements CinemaControllerHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CinemaService cinemaService;
    @MockBean
    private MovieService movieService;
    @MockBean
    private CinemaMapper cinemaMapper;

    @Test
    @DisplayName("주변 영화관 찾기 테스트")
    public void getNearCinemaTest() throws Exception {
        // given
        String x = "126.905217202756";
        String y = "37.4863264672324";
        String distance = "3000";
        String mega = "true";
        String lotte = "true";
        String cgv = "true";
        String movieId = "1";
        String date = "2023-01-01";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("x", x);
        params.add("y", y);
        params.add("distance", distance);
        params.add("mega", mega);
        params.add("lotte", lotte);
        params.add("cgv", cgv);
        params.add("movieId", movieId);
        params.add("date", date);

        given(movieService.findMovie(Mockito.anyLong())).willReturn(new Movie());

        given(cinemaService.
                find(
                        Mockito.anyDouble(),
                        Mockito.anyDouble(),
                        Mockito.anyDouble(),
                        Mockito.anyBoolean(),
                        Mockito.anyBoolean(),
                        Mockito.anyBoolean(),
                        Mockito.any(Movie.class),
                        Mockito.any(LocalDate.class))
        ).willReturn(StubData.getCinemaPlusList());

        given(cinemaMapper.cinemaPlusListToNearResponse(Mockito.any(Movie.class), Mockito.anyList())).willReturn(StubData.getNearResponse());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(GET_CINEMA_URL, params));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());

    }
}
