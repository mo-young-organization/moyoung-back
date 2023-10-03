package Moyoung.Server.chat.service;

import Moyoung.Server.chat.entity.ChatRoomMembersInfo;
import Moyoung.Server.chat.repository.ChatRoomMembersInfoRepository;
import Moyoung.Server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomMembersInfoRepository chatRoomMembersInfoRepository;

    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    public void setSessionId(long recruitingArticleId, Member member, String sessionId) {
        ChatRoomMembersInfo chatRoomMembersInfo = chatRoomMembersInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(member.getMemberId(), recruitingArticleId).get();
        chatRoomMembersInfo.setSessionId(sessionId);
        chatRoomMembersInfo.setUnreadMessageCount(0);
        chatRoomMembersInfoRepository.save(chatRoomMembersInfo);
    }
}
