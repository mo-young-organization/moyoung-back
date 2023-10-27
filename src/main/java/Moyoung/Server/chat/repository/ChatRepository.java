package Moyoung.Server.chat.repository;

import Moyoung.Server.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.recruitingArticle.recruitingArticleId = :recruitingArticleId AND c.chatTime > :entryDate")
    List<Chat> findChatsByRecruitingArticleAndEntryDate(long recruitingArticleId, LocalDateTime entryDate);
}
