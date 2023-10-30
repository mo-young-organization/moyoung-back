package Moyoung.Server.helper;

import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.member.dto.MemberDto;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StubData {
    public static class MockSecurity {
        public static String getValidAccessToken(String secretKey) {
            JwtTokenizer jwtTokenizer = new JwtTokenizer();
            Map<String, Object> claims = new HashMap<>();
            claims.put("memberId", 1L);

            String subject = "test access token";
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
            Date expiration = calendar.getTime();

            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);

            String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

            return accessToken;
        }
    }

    public static String getPostMemberContent() {
        Gson gson = new Gson();
        MemberDto.Post post = new MemberDto.Post();
        post.setId("test");
        post.setPassword("1234");

        return gson.toJson(post);
    }

    public static String getPostInfoContent() {
        Gson gson = new Gson();
        MemberDto.Info info = new MemberDto.Info();
        info.setDisplayName("test");
        info.setGender(true);
        info.setAge(1);

        return gson.toJson(info);
    }

    public static String getCheckDisplayNameContent() {
        Gson gson = new Gson();
        MemberDto.DisplayName displayName = new MemberDto.DisplayName();
        displayName.setDisplayName("test");

        return gson.toJson(displayName);
    }
}
