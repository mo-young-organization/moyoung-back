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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitingArticleService {
    private final RecruitingArticleRepository recruitingArticleRepository;
    private final MemberService memberService;
    private final RunningTimeService runningTimeService;

    // 게시글 등록
    public void registerRecruitingArticle(RecruitingArticle recruitingArticle, long memberId) {
        Member member = memberService.findVerifiedMember(memberId);
        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(recruitingArticle.getRunningTime().getRunningTimeId());
        recruitingArticle.setMember(member);
        recruitingArticle.addParticipant(member);
        recruitingArticle.setRunningTime(runningTime);
        recruitingArticleRepository.save(recruitingArticle);
    }

    // 게시글 수정
    public void updateRecruitingArticle(RecruitingArticle recruitingArticle, long memberId) {
        RecruitingArticle findedRecruitingArticle = findVerifiedRecruitingArticle(recruitingArticle.getRecruitingArticleId());

        checkAuthor(memberId, findedRecruitingArticle.getMember().getMemberId());

        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(recruitingArticle.getRunningTime().getRunningTimeId());
        findedRecruitingArticle.setTitle(recruitingArticle.getTitle());
        findedRecruitingArticle.setRunningTime(runningTime);
        findedRecruitingArticle.setMaxNum(recruitingArticle.getMaxNum());
        findedRecruitingArticle.setGender(recruitingArticle.getGender());
        findedRecruitingArticle.setAge(recruitingArticle.getAge());
        findedRecruitingArticle.setModifiedAt(LocalDateTime.now());
        recruitingArticleRepository.save(findedRecruitingArticle);
    }

    // 게시글 리스트
    public Page<RecruitingArticle> getRecruitingArticleList(int page) {
        return recruitingArticleRepository.findAll(PageRequest.of(page - 1, 20, Sort.by("recruitingArticleId").descending()));
    }

    // 게시글 삭제
    public void deleteRecruitingArticle(long recruitingArticleId, long memberId) {
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(recruitingArticleId);

        checkAuthor(memberId, recruitingArticle.getMember().getMemberId());

        recruitingArticleRepository.delete(recruitingArticle);
    }

    // 검증된 게시글 찾기
    public RecruitingArticle findVerifiedRecruitingArticle(long recruitingArticleId) {
        Optional<RecruitingArticle> optionalRecruitingArticle = recruitingArticleRepository.findById(recruitingArticleId);
        RecruitingArticle recruitingArticle = optionalRecruitingArticle.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return recruitingArticle;
    }

    // 작성자 확인
    private static void checkAuthor(long memberId, long authorId) {
        if (authorId != memberId) {
            throw new BusinessLogicException(ExceptionCode.ONLY_AUTHOR);
        }
    }
}
