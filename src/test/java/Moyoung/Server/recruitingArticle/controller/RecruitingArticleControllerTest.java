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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    @Test
    @DisplayName("비로그인 게시글 불러오기 (필터 적용 X)")
    public void getRecruitingArticlesNonLoginTest() throws Exception {
        // given
        String page = "1";
        String size = "10";
        String x = "126.692549074164";
        String y = "37.7180926232465";
        String keyword = "keyword";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", page);
        params.add("size", size);
        params.add("x", x);
        params.add("y", y);
        params.add("keyword", keyword);

        given(recruitingArticleService.getRecruitingArticleListNonLogin(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyString())).willReturn(StubData.getPageRecruitingArticle());

        given(recruitingArticleMapper.recruitingArticlesToList(Mockito.anyList())).willReturn(StubData.getRecruitingArticleResponses());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(RECRUITING_ARTICLE_NON_LOGIN_GET_URL, params));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 게시글 불러오기 (필터 적용 가능)")
    public void getRecruitingArticlesTest() throws Exception {
        // given
        String page = "1";
        String size = "10";
        String gender = "1";
        String teenager = "true";
        String twenties = "true";
        String thirties = "true";
        String x = "126.692549074164";
        String y = "37.7180926232465";
        String distance = "1000";
        String keyword = "keyword";
        String sort = "false";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", page);
        params.add("size", size);
        params.add("gender", gender);
        params.add("teenager", teenager);
        params.add("twenties", twenties);
        params.add("thirties", thirties);
        params.add("x", x);
        params.add("y", y);
        params.add("distance", distance);
        params.add("keyword", keyword);
        params.add("sort", sort);

        given(recruitingArticleService.getRecruitingArticleList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyString(), Mockito.anyBoolean())).willReturn(StubData.getPageRecruitingArticle());

        given(recruitingArticleMapper.recruitingArticlesToList(Mockito.anyList())).willReturn(StubData.getRecruitingArticleResponses());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(RECRUITING_ARTICLE_URL, params, accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 정보 불러오기 테스트")
    public void getRecruitingArticleTest() throws Exception {
        // given
        given(recruitingArticleService.getRecruitingArticle(Mockito.anyLong(), Mockito.anyLong())).willReturn(new RecruitingArticle());

        given(recruitingArticleMapper.recruitingArticleToResponse(Mockito.any(RecruitingArticle.class))).willReturn(StubData.getRecruitingArticleResponse());

        // when
        ResultActions actions =
                mockMvc.perform(getRequestBuilder(RECRUITING_ARTICLE_RESOURCE_URI, 1L, accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deleteRecruitingArticleTest() throws Exception {
        // given
        doNothing().when(recruitingArticleService).deleteRecruitingArticle(Mockito.anyLong(), Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(deleteRequestBuilder(RECRUITING_ARTICLE_RESOURCE_URI, 1L, accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 입장 테스트")
    public void enterArticleTest() throws Exception {
        // given
        doNothing().when(recruitingArticleService).enterRecruit(Mockito.anyLong(), Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(postRequestBuilder(RECRUITING_ARTICLE_ENTER_URL, 1L, accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }
}
