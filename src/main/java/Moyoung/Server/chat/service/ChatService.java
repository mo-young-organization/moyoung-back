package Moyoung.Server.chat.service;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.repository.ChatRepository;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.repository.RecruitingArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final RecruitingArticleRepository recruitingArticleRepository;

    // 채팅 보내기
    public Chat saveChat(Chat chat) {
        Member member = memberService.findVerifiedMember(chat.getSender().getMemberId());
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(chat.getRecruitingArticle().getRecruitingArticleId());
        if (recruitingArticle.getMember().getMemberId() != member.getMemberId() && !recruitingArticle.getMembersEntryDate().containsKey(member)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }
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

//    // 채팅 불러오기
//    public Page<Chat> loadChat(long recruitingArticleId, long memberId, int page) {
//        RecruitingArticle recruitingArticle = recruitingArticleService.findVerifiedRecruitingArticle(recruitingArticleId);
//        Member member = memberService.findVerifiedMember(memberId);
//        if(!recruitingArticle.getParticipants().contains(member)) {
//            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
//        }
//
//        return chatRepository.findAllByRecruitingArticleRecruitingArticleId(recruitingArticleId, PageRequest.of(page - 1, 20, Sort.by("chatId").descending()));
//    }

    // 채팅 불러오기
    public List<Chat> getChatMessage(long recruitArticleId) {
        return chatRepository.findAllByRecruitingArticleRecruitingArticleId(recruitArticleId);
    }
}
