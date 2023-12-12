package Moyoung.Server.auth.interceptor;

import Moyoung.Server.auth.utils.ErrorResponder;
import Moyoung.Server.auth.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Component
public class JwtParseInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private static final ThreadLocal<Long> authenticatedMemberId = new ThreadLocal<>();

    public JwtParseInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public static long getAuthenticatedMemberId() {
        return authenticatedMemberId.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
//            if (request.getMethod().equals("GET")) return  true;
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                // 유효하지 않은 토큰 처리
                ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
                return false;
            }

            String jws = authorizationHeader.substring(7);
            // 토큰 검증
            jwtUtils.validateToken(jws);

            Map<String, Object> claims = jwtUtils.getJwsClaimsFromRequest(request);
            Object memberId = claims.get("memberId");
            if (memberId != null) {
                authenticatedMemberId.set(Long.valueOf(memberId.toString()));
                return true;
            } else {
                // memberId가 null 인 경우에 대한 예외 처리
                ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
                return false;
            }
        } catch (Exception e) {
            // 예외 처리
            ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        authenticatedMemberId.remove();
    }
}
