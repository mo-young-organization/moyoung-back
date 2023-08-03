package Moyoung.Server.recruitingarticle.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.mapper.RecruitingArticleMapper;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitingArticleController {
    private final RecruitingArticleService recruitingArticleService;
    private final RecruitingArticleMapper recruitingArticleMapper;

    @PostMapping("/recruit")
    public ResponseEntity postRecruitingArticle(@RequestBody RecruitingArticleDto.Post requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        recruitingArticleService.registerRecruitingArticle(recruitingArticleMapper.postToRecruitingArticle(requestBody), authenticationMemberId);
        return new ResponseEntity<>("게시글 등록이 완료되었습니다.", HttpStatus.OK);
    }
}
