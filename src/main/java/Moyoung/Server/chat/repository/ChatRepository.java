package Moyoung.Server.chat.repository;

import Moyoung.Server.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findAllByRecruitingArticleRecruitingArticleId(long recruitingArticleId, Pageable pageable);
    List<Chat> findByRecruitingArticleRecruitingArticleId(long recruitingArticleId);
    List<Chat> findByChatIdAfterAndRecruitingArticleRecruitingArticleId(long chatId, long recruitingArticleId);
    List<Chat> findBySender(String sender);
}
