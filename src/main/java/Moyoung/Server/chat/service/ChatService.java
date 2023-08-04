package Moyoung.Server.chat.service;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.repository.ChatRepository;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final RecruitingArticleService recruitingArticleService;

    // 채팅 보내기
    public void SendChat(Chat chat) {
        Member member = memberService.findVerifiedMember(chat.getSender().getMemberId());
        RecruitingArticle recruitingArticle = recruitingArticleService.findVerifiedRecruitingArticle(chat.getRecruitingArticle().getRecruitingArticleId());
        if(!recruitingArticle.getParticipants().contains(member)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }
        chat.setSender(member);
        chat.setRecruitingArticle(recruitingArticle);

        chatRepository.save(chat);
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
}
