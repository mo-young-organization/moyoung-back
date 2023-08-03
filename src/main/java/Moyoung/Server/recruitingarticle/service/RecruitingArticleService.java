package Moyoung.Server.recruitingarticle.service;

import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.repository.RecruitingArticleRepository;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.service.RunningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitingArticleService {
    private final RecruitingArticleRepository recruitingArticleRepository;
    private final MemberService memberService;
    private final RunningTimeService runningTimeService;

    public void registerRecruitingArticle(RecruitingArticle recruitingArticle, long memberId) {
        Member member = memberService.findVerifiedMember(memberId);
        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(recruitingArticle.getRunningTime().getRunningTimeId());
        recruitingArticle.setMember(member);
        recruitingArticle.addParticipant(member);
        recruitingArticle.setRunningTime(runningTime);
        recruitingArticleRepository.save(recruitingArticle);
    }

    public RecruitingArticle findVerifiedRecruitingArticle(long recruitingArticleId) {
        Optional<RecruitingArticle> optionalRecruitingArticle = recruitingArticleRepository.findById(recruitingArticleId);
        RecruitingArticle recruitingArticle = optionalRecruitingArticle.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return recruitingArticle;
    }
}
