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
import java.util.List;
import java.util.Optional;

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

        return chatRepository.findChatsByRecruitingArticleAndEntryDate(recruitArticleId, entryDate);
    }

    private ChatRoomInfo findChatRoomInfo(long recruitingArticleId, long memberId) {
        Optional<ChatRoomInfo> optionalChatRoomInfo = chatRoomInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(memberId, recruitingArticleId);
        return optionalChatRoomInfo.orElseThrow(() -> new BusinessLogicException(ExceptionCode.UNAUTHORIZED));
    }
}
