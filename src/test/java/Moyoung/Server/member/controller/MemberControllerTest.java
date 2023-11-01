package Moyoung.Server.member.controller;

import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.helper.MemberControllerHelper;
import Moyoung.Server.helper.StubData;
import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.mapper.MemberMapper;
import Moyoung.Server.member.service.MemberService;
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

@WebMvcTest(MemberController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberControllerTest implements MemberControllerHelper {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberMapper mapper;

    private  String accessToken;

    @BeforeAll
    public void init() {
        accessToken = StubData.MockSecurity.getValidAccessToken(jwtTokenizer.getSecretKey());
    }

    @Test
    @DisplayName("Member Create Test")
    public void createMemberTest() throws Exception {
        // given
        given(mapper.postToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());

        doNothing().when(memberService).joinInLocal(Mockito.any(Member.class));

        // when
        ResultActions actions =
                mockMvc.perform(postRequestBuilder(SIGN_UP_URL, StubData.getPostMemberContent()));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Info Insert Test")
    public void postInformationTest() throws Exception {
        // given
        given(mapper.infoToMember(Mockito.any(MemberDto.Info.class))).willReturn(new Member());

        given(memberService.registerInformation(Mockito.anyLong(), Mockito.any(Member.class))).willReturn("test");

        // when
        ResultActions actions =
                mockMvc.perform(postRequestBuilder(INFO_URL, StubData.getPostInfoContent(), accessToken));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Check DisplayName Test")
    public void checkDisplayNameTest() throws Exception {
        // given
        given(memberService.checkDisplayName(Mockito.any(MemberDto.DisplayName.class))).willReturn(true);

        // when
        ResultActions actions =
                mockMvc.perform(postRequestBuilder(DISPLAY_NAME_URL, StubData.getCheckDisplayNameContent()));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Member Delete Test")
    public void deleteMemberTest() throws Exception {
        // given
        doNothing().when(memberService).deleteMember(Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(deleteRequestBuilder(MEMBER_URL, accessToken));

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
