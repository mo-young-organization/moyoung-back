package Moyoung.Server.chat.service;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomInfo;
import Moyoung.Server.chat.repository.ChatRepository;
import Moyoung.Server.chat.repository.ChatRoomInfoRepository;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.repository.RecruitingArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final RecruitingArticleRepository recruitingArticleRepository;
    private final ChatRoomInfoRepository chatRoomInfoRepository;

    // 채팅 보내기
    public Chat saveChat(Chat chat) {
        Member member = memberService.findVerifiedMember(chat.getSender().getMemberId());
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(chat.getRecruitingArticle().getRecruitingArticleId());
        // 참여 인원인지 확인
        findChatRoomInfo(recruitingArticle.getRecruitingArticleId(), member.getMemberId());

        chat.setSender(member);
        chat.setRecruitingArticle(recruitingArticle);

        // 대화 참여인원 채팅방 정보 갱신
        // if chat.Type.equals CHAT
        if (chat.getType().equals(Chat.Type.CHAT)) {
            List<ChatRoomInfo> chatRoomInfoList = chatRoomInfoRepository.findAllByRecruitingArticleRecruitingArticleId(recruitingArticle.getRecruitingArticleId());
            chatRoomInfoList.forEach(chatRoomInfo -> {
                chatRoomInfo.setLastMessage(chat.getContent());
                chatRoomInfo.setLastMessageAt(chat.getChatTime());
                chatRoomInfo.plusUnreadCount();
                chatRoomInfoRepository.save(chatRoomInfo);
            });
        }

        if (chat.getType().equals(Chat.Type.EXIT)) {
            ChatRoomInfo chatRoomInfo = findChatRoomInfo(recruitingArticle.getRecruitingArticleId(), member.getMemberId());
            chatRoomInfoRepository.delete(chatRoomInfo);
        }

        return chatRepository.save(chat);

    }

    // 순환참조 방지용 중복 메서드
    private RecruitingArticle findVerifiedRecruitingArticle(long recruitingArticleId) {
        Optional<RecruitingArticle> optionalRecruitingArticle = recruitingArticleRepository.findById(recruitingArticleId);
        RecruitingArticle recruitingArticle = optionalRecruitingArticle.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return recruitingArticle;
    }

    // 채팅 불러오기
    public List<Chat> getChatMessage(long recruitArticleId, long memberId) {
        // 모집글 참여 시간 추출
        ChatRoomInfo chatRoomInfo = findChatRoomInfo(recruitArticleId, memberId);
        LocalDateTime entryDate = chatRoomInfo.getEntryTime();

        // unreadCount 초기화
        chatRoomInfo.resetUnreadCount();
        chatRoomInfoRepository.save(chatRoomInfo);

        return chatRepository.findChatsByRecruitingArticleAndEntryDate(recruitArticleId, entryDate);
    }

    // 채팅방 리스트 불러오기
    public List<ChatRoomInfo> getChatRoomList(long memberId) {
        List<ChatRoomInfo> chatRoomInfoList = chatRoomInfoRepository.findAllByMemberMemberId(memberId);

        // 최근 메세지가 추가된 시간을 기준으로 정렬
        return chatRoomInfoList.stream()
                .sorted(Comparator.comparing(ChatRoomInfo::getLastMessageAt).reversed())
                .collect(Collectors.toList());
    }

    private ChatRoomInfo findChatRoomInfo(long recruitingArticleId, long memberId) {
        Optional<ChatRoomInfo> optionalChatRoomInfo = chatRoomInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(memberId, recruitingArticleId);
        return optionalChatRoomInfo.orElseThrow(() -> new BusinessLogicException(ExceptionCode.UNAUTHORIZED));
    }
}
