package Moyoung.Server.auth.jwt;

import Moyoung.Server.auth.refresh.RefreshToken;
import Moyoung.Server.auth.refresh.RefreshTokenRepository;
import Moyoung.Server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenizer jwtTokenizer;

    public String delegateAccessToken(Member member) {
        String id = member.getId();

        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        claims.put("id", member.getId());

        List<String> roles = member.getRoles();

        claims.put("roles", roles);

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, id, expiration, base64EncodedSecretKey);

        return "Bearer " + accessToken;
    }

    public String delegateRefreshToken(Member member) {
        String subject = member.getId();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        RefreshToken rtk = new RefreshToken(refreshToken, member.getMemberId());
        refreshTokenRepository.save(rtk);

        return "Bearer " + refreshToken;
    }

    public Date getAccessTokenExpiration() {
        return jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
    }

    public Date getRefreshTokenExpiration() {
        return jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
    }
}
