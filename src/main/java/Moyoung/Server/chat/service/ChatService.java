package Moyoung.Server.chat.service;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.repository.ChatRepository;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.repository.MemberRepository;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
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
}
