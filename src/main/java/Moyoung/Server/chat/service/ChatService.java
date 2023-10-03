package Moyoung.Server.chat.service;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomMembersInfo;
import Moyoung.Server.chat.repository.ChatRepository;
import Moyoung.Server.chat.repository.ChatRoomMembersInfoRepository;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.repository.RecruitingArticleRepository;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomMembersInfoRepository chatRoomMembersInfoRepository;
    private final MemberService memberService;
    private final RecruitingArticleService recruitingArticleService;
    private final RecruitingArticleRepository recruitingArticleRepository;
    private final SimpMessageSendingOperations sendingOperations;

    // 채팅 보내기
    public void SendChat(long memberId, long recruitArticleId, Chat chat) {
        Member member = memberService.findVerifiedMember(memberId);
        RecruitingArticle recruitingArticle = recruitingArticleService.findVerifiedRecruitingArticle(recruitArticleId);
        List<ChatRoomMembersInfo> chatRoomMembersInfos = chatRoomMembersInfoRepository.findByRecruitingArticleRecruitingArticleId(recruitArticleId);
        chat.setSender(member.getDisplayName());
        chat.setCreatedAt(LocalDateTime.now());
        chat.setRecruitingArticle(recruitingArticle);
        chat.setUnreadCount(setUnreadMessageCount(recruitArticleId));
        Chat savedChat = chatRepository.save(chat);

        for (int i = 0; i < chatRoomMembersInfos.size(); i++) {
            ChatRoomMembersInfo chatRoomMembersInfo = chatRoomMembersInfos.get(i);
            if (chatRoomMembersInfo.getSessionId() != null) {
                chatRoomMembersInfo.setLastMessageId(savedChat.getChatId());
            }
        }

        chatRoomMembersInfoRepository.saveAll(chatRoomMembersInfos);
        recruitingArticle.setLastChat(chat.getContent());
        recruitingArticle.setLastChatCreated(savedChat.getCreatedAt());
        recruitingArticleRepository.save(recruitingArticle);

        sendingOperations.convertAndSend("/receive/chat/" + recruitArticleId, savedChat);
    }

    // 채팅 불러오기
    public Page<Chat> loadChat(long recruitingArticleId, long memberId, int page) {
        RecruitingArticle recruitingArticle = recruitingArticleService.findVerifiedRecruitingArticle(recruitingArticleId);
        Member member = memberService.findVerifiedMember(memberId);
        if(!recruitingArticle.getParticipants().contains(member)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }

        return chatRepository.findAllByRecruitingArticleRecruitingArticleId(recruitingArticleId, PageRequest.of(page - 1, 20, Sort.by("chatId").descending()));
    }

    private int setUnreadMessageCount(Long recruitingArticleId) {
        List<ChatRoomMembersInfo> chatRoomMembersInfos = chatRoomMembersInfoRepository.findByRecruitingArticleRecruitingArticleId(recruitingArticleId);
        int count = 0;
        for (int i = 0; i < chatRoomMembersInfos.size(); i++) {
            if (chatRoomMembersInfos.get(i).getSessionId() == null) {
                chatRoomMembersInfos.get(i).setUnreadMessageCount(chatRoomMembersInfos.get(i).getUnreadMessageCount() + 1);
                count++;
            }
        }

        chatRoomMembersInfoRepository.saveAll(chatRoomMembersInfos);

        return count;
    }
}
