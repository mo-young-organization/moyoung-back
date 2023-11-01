package Moyoung.Server.recruitingarticle.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.dto.MultiResponseDto;
import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.mapper.RecruitingArticleMapper;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class RecruitingArticleController {
    private final RecruitingArticleService recruitingArticleService;
    private final RecruitingArticleMapper recruitingArticleMapper;

    @PostMapping("/recruit")
    public ResponseEntity postRecruitingArticle(@RequestBody RecruitingArticleDto.PostPatch requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        recruitingArticleService.registerRecruitingArticle(recruitingArticleMapper.postToRecruitingArticle(requestBody), authenticationMemberId);
        return new ResponseEntity<>("게시글 등록이 완료되었습니다.", HttpStatus.OK);
    }

    @PatchMapping("/recruit/{recruit-id}")
    public ResponseEntity patchRecruitingArticle(@RequestBody RecruitingArticleDto.PostPatch requestBody,
                                                 @PathVariable("recruit-id") long recruitingArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        recruitingArticleService.updateRecruitingArticle(recruitingArticleMapper.patchToRecruitingArticle(requestBody, recruitingArticleId), authenticationMemberId);
        return new ResponseEntity<>("게시글 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 비로그인 게시물 불러오기(필터 x), 인터셉터를 활용 안하기 때문에 다른 엔드포인트 활용
    @GetMapping("/recruit-article")
    public ResponseEntity getRecruitingArticlesNonLogin(@Positive @RequestParam int page,
                                                        @RequestParam(required = false) String keyword) {
        Page<RecruitingArticle> pageRecruitingArticle = recruitingArticleService.getRecruitingArticleListNonLogin(page, keyword);
        List<RecruitingArticle> recruitingArticles = pageRecruitingArticle.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(recruitingArticleMapper.recruitingArticlesToList(recruitingArticles), pageRecruitingArticle), HttpStatus.OK);
    }

    @GetMapping("/recruit")
    public ResponseEntity getRecruitingArticles(@Positive @RequestParam int page,
                                                @Positive @RequestParam int size,
                                                @RequestParam(required = false) Integer gender,
                                                @RequestParam(required = false) Boolean teenager,
                                                @RequestParam(required = false) Boolean twenties,
                                                @RequestParam(required = false) Boolean thirties,
                                                @RequestParam double x,
                                                @RequestParam double y,
                                                @RequestParam(required = false) Double distance,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Boolean sort) {
        Page<RecruitingArticle> pageRecruitingArticle = recruitingArticleService.getRecruitingArticleList(page, size, gender, teenager, twenties, thirties, x, y, distance, keyword, sort);
        List<RecruitingArticle> recruitingArticles = pageRecruitingArticle.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(recruitingArticleMapper.recruitingArticlesToList(recruitingArticles), pageRecruitingArticle), HttpStatus.OK);
    }

    @DeleteMapping("/recruit/{recruit-id}")
    public ResponseEntity deleteRecruitingArticle(@PathVariable("recruit-id") long recruitingArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        recruitingArticleService.deleteRecruitingArticle(recruitingArticleId, authenticationMemberId);
        return new ResponseEntity<>("게시글 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/recruit/{recruit-id}/enter")
    public ResponseEntity enterArticle(@PathVariable("recruit-id") long recruitingArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();

        recruitingArticleService.enterRecruit(recruitingArticleId, authenticationMemberId);
        return new ResponseEntity<>("모집글 입장이 완료되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/recruit/{recruit-id}/leave")
    public ResponseEntity leaveArticle(@PathVariable("recruit-id") long recruitingArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();

        recruitingArticleService.leaveRecruit(recruitingArticleId, authenticationMemberId);
        return new ResponseEntity<>("모집글 퇴장이 완료되었습니다.", HttpStatus.OK);
    }
}
