package Moyoung.Server.auth.handler;

import Moyoung.Server.auth.jwt.TokenService;
import Moyoung.Server.auth.userinfo.GoogleUserInfo;
import Moyoung.Server.auth.userinfo.KakaoUserInfo;
import Moyoung.Server.auth.userinfo.NaverUserInfo;
import Moyoung.Server.auth.userinfo.OAuth2UserInfo;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;
    private final TokenService tokenService;

    @Value("${client.callback}")
    private String url;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuth2User = (OAuth2User)authentication.getPrincipal();
        OAuth2UserInfo oAuth2UserInfo = null;
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;

        String provider = oauth2Token.getAuthorizedClientRegistrationId();

        if(provider.equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String id = provider + "_" + providerId;
        Member member = null;

        try {
            member = saveMember(id);
        } catch (Exception e) {
            member = memberService.findMemberById(id);
        } finally {
            redirect(request, response, member);
        }
    }

    private Member saveMember(String id) {
        Member member = new Member(id);
        return memberService.joinInOauth(member);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, Member member) throws IOException {
        String accessToken = tokenService.delegateAccessToken(member);
        String refreshToken = tokenService.delegateRefreshToken(member);

        Date accessTokenExpiration = tokenService.getAccessTokenExpiration();
        Date refreshTokenExpiration = tokenService.getRefreshTokenExpiration();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String accessTokenExpirationFormatted = dateFormat.format(accessTokenExpiration);
        String refreshTokenExpirationFormatted = dateFormat.format(refreshTokenExpiration);

        String uri;

        if (member.getDisplayName() == null) {
            uri = createInterestUri(false, accessToken, refreshToken, member.getMemberId(), accessTokenExpirationFormatted, refreshTokenExpirationFormatted).toString();
        } else {
            uri = createUri(true, accessToken, refreshToken, member.getMemberId(), member.getDisplayName(), accessTokenExpirationFormatted, refreshTokenExpirationFormatted).toString();
        }


        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    // 콜백 Uri
    private URI createUri(boolean userInfoCheck, String accessToken, String refreshToken, long memberId, String displayName, String accessTokenExpiration, String refreshTokenExpiration) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("user", userInfoCheck)
                .queryParam("Authorization", accessToken)
                .queryParam("Refresh", refreshToken)
                .queryParam("memberId", memberId)
                .queryParam("displayName", URLEncoder.encode(displayName, UTF_8))
                .queryParam("accessTokenExpiration", accessTokenExpiration)
                .queryParam("refreshTokenExpiration", refreshTokenExpiration);
        return builder.build().toUri();
    }

    // 콜백 Uri
    private URI createInterestUri(boolean userInfoCheck, String accessToken, String refreshToken, long memberId, String accessTokenExpiration, String refreshTokenExpiration) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("user", userInfoCheck)
                .queryParam("Authorization", accessToken)
                .queryParam("Refresh", refreshToken)
                .queryParam("memberId", memberId)
                .queryParam("accessTokenExpiration", accessTokenExpiration)
                .queryParam("refreshTokenExpiration", refreshTokenExpiration);
        return builder.build().toUri();
    }
}
