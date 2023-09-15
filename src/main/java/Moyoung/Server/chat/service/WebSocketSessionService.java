package Moyoung.Server.chat.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionService {

    private final Map<String, UserSessionInfo> sessionMap = new ConcurrentHashMap<>();

    public void registerSession(String sessionId, String displayName, String recruitingArticleId) {
        sessionMap.put(sessionId, new UserSessionInfo(displayName, recruitingArticleId));
    }

    public UserSessionInfo getSessionInfo(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static class UserSessionInfo {
        private final String displayName;
        private final String recruitingArticleId;

        public UserSessionInfo(String displayName, String recruitingArticleId) {
            this.displayName = displayName;
            this.recruitingArticleId = recruitingArticleId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getRecruitingArticleId() {
            return recruitingArticleId;
        }
    }
}
