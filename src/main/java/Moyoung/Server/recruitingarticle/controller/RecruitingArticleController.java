package Moyoung.Server.recruitingarticle.controller;

import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitingArticleController {
    private final RecruitingArticleService recruitingArticleService;
}
