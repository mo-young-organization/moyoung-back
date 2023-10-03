package Moyoung.Server.stomp;

import Moyoung.Server.auth.jwt.JwtTokenizer;
import Moyoung.Server.auth.utils.JwtUtils;
import Moyoung.Server.chat.entity.ChatRoomMembersInfo;
import Moyoung.Server.chat.repository.ChatRoomMembersInfoRepository;
import Moyoung.Server.chat.service.ChatRoomService;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtUtils jwtUtils;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomMembersInfoRepository chatRoomMembersInfoRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("Authorization");
            jwtUtils.validateToken(jwt);

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("Authorization");
            Member member = memberService.findVerifiedMember(Long.parseLong(jwtUtils.getUserIdFromToken(jwt)));
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            String recruitingArticleId = chatRoomService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            chatRoomService.setSessionId(Long.parseLong(recruitingArticleId),member,sessionId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            ChatRoomMembersInfo chatRoomMembersInfo = chatRoomMembersInfoRepository.findBySessionId(sessionId);
            chatRoomMembersInfo.setSessionId(null);
            chatRoomMembersInfoRepository.save(chatRoomMembersInfo);
        }

        return message;
    }
}
