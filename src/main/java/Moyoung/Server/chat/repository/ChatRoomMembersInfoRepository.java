package Moyoung.Server.chat.repository;

import Moyoung.Server.chat.entity.ChatRoomMembersInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMembersInfoRepository extends JpaRepository<ChatRoomMembersInfo, Long> {
    List<ChatRoomMembersInfo> findByRecruitingArticleRecruitingArticleId(long recruitingArticleId);
    List<ChatRoomMembersInfo> findByMemberMemberId(long memberId);
    Optional<ChatRoomMembersInfo> findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(long memberId, long recruitingArticleId);
    ChatRoomMembersInfo findBySessionId(String sessionId);
}
