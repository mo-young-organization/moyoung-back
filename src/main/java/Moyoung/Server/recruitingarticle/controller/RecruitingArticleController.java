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

    @GetMapping("/recruit")
    public ResponseEntity getRecruitingArticles(@Positive @RequestParam int page) {
        Page<RecruitingArticle> pageRecruitingArticle = recruitingArticleService.getRecruitingArticleList(page);
        List<RecruitingArticle> recruitingArticles = pageRecruitingArticle.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(recruitingArticleMapper.recruitingArticlesToList(recruitingArticles), pageRecruitingArticle), HttpStatus.OK);
    }

    @DeleteMapping("/recruit/{recruit-id}")
    public ResponseEntity deleteRecruitingArticle(@PathVariable("recruit-id") long recruitingArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        recruitingArticleService.deleteRecruitingArticle(recruitingArticleId, authenticationMemberId);
        return new ResponseEntity<>("게시글 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/recruit/{recruit-id}/enter")
    public ResponseEntity enterRecruitingArticle(@PathVariable("recruit-id") long recruitingArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();

        return new ResponseEntity<>(recruitingArticleService.enterRecruit(recruitingArticleId, authenticationMemberId), HttpStatus.OK);
    }
}
