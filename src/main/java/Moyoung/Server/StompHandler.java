package Moyoung.Server;

import Moyoung.Server.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("message: {}", message);
        // websocket 연결시 헤더의 jwt token 유효성 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {

            jwtUtils.validateToken(accessor.getNativeHeader("Authorization").toString().substring(7));
        }
        return message;
    }
}
