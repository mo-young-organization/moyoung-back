package Moyoung.Server.chat.repository;

import Moyoung.Server.chat.entity.ChatRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomInfoRepository extends JpaRepository<ChatRoomInfo, Long> {
    Optional<ChatRoomInfo> findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(long memberId, long recruitingArticleId);
    List<ChatRoomInfo> findAllByMemberMemberId(long memberId);
    List<ChatRoomInfo> findAllByRecruitingArticleRecruitingArticleId(long recruitingArticleId);
}
