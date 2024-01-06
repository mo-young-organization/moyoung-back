package Moyoung.Server.runningTime.controller;

import Moyoung.Server.helper.RunningTimeControllerHelper;
import Moyoung.Server.helper.StubData;
import Moyoung.Server.runningtime.controller.RunningTimeController;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.mapper.RunningTimeMapper;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunningTimeController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
public class RunningTimeControllerTest implements RunningTimeControllerHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RunningTimeService runningTimeService;
    @MockBean
    private RunningTimeMapper runningTimeMapper;

    @Test
    @DisplayName("영화 상영정보 내부 모집글 리스트 불러오기 테스트")
    public void getArticleListInRunningTimeTest() throws Exception {
        // given
        given(runningTimeService.findVerifiedRunningTime(Mockito.anyLong())).willReturn(new RunningTime());

        given(runningTimeMapper.runningTimeToRunningTimeListResponse(Mockito.any(RunningTime.class))).willReturn(StubData.getRunningTimeArticleList());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(RUNNING_TIME_ARTICLE_URI, 1));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }
}
