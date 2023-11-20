package Moyoung.Server.recruitingArticle.controller;

import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.helper.RecruitingArticleControllerHelper;
import Moyoung.Server.helper.StubData;
import Moyoung.Server.recruitingarticle.controller.RecruitingArticleController;
import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.mapper.RecruitingArticleMapper;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecruitingArticleController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecruitingArticleControllerTest implements RecruitingArticleControllerHelper {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @MockBean
    private RecruitingArticleService recruitingArticleService;
    @MockBean
    private RecruitingArticleMapper recruitingArticleMapper;

    private String accessToken;

    @BeforeAll
    public void init() {
        accessToken = StubData.MockSecurity.getValidAccessToken(jwtTokenizer.getSecretKey());
    }
    @Test
    @DisplayName("게시글 등록 테스트")
    public void postRecruitingArticleTest() throws Exception {
        // given
        given(recruitingArticleMapper.postToRecruitingArticle(Mockito.any(RecruitingArticleDto.PostPatch.class))).willReturn(new RecruitingArticle());

        doNothing().when(recruitingArticleService).registerRecruitingArticle(Mockito.any(RecruitingArticle.class), Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(postRequestBuilder(RECRUITING_ARTICLE_URL, StubData.getPostRecruitingArticleContent(), accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void patchRecruitingArticleTest() throws Exception {
        // given
        given(recruitingArticleMapper.patchToRecruitingArticle(Mockito.any(RecruitingArticleDto.PostPatch.class), Mockito.anyLong())).willReturn(new RecruitingArticle());

        doNothing().when(recruitingArticleService).updateRecruitingArticle(Mockito.any(RecruitingArticle.class), Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(patchRequestBuilder(RECRUITING_ARTICLE_RESOURCE_URI, 1L, StubData.getPostRecruitingArticleContent(), accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }
}
