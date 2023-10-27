package Moyoung.Server.auth.filter;

import Moyoung.Server.auth.dto.LoginDto;
import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.auth.jwt.TokenService;
import Moyoung.Server.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final JwtTokenizer jwtTokenizer;


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Member member = (Member) authResult.getPrincipal();

        String accessToken = tokenService.delegateAccessToken(member);
        String refreshToken = tokenService.delegateRefreshToken(member);

        Date accessTokenExpiration = tokenService.getAccessTokenExpiration();
        Date refreshTokenExpiration = tokenService.getRefreshTokenExpiration();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String accessTokenExpirationFormatted = dateFormat.format(accessTokenExpiration);
        String refreshTokenExpirationFormatted = dateFormat.format(refreshTokenExpiration);

        response.setHeader("Authorization", accessToken);
        response.setHeader("Refresh", refreshToken);
        response.setHeader("accessTokenExpiration", accessTokenExpirationFormatted);
        response.setHeader("refreshTokenExpiration", refreshTokenExpirationFormatted);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("displayName", member.getDisplayName());
        responseData.put("memberId", member.getMemberId());
        responseData.put("age", member.getAge().getAge());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponseBody = objectMapper.writeValueAsString(responseData);

        response.setContentType("application/json");

        response.getWriter().write(jsonResponseBody);
        response.getWriter().flush();

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
