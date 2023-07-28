package Moyoung.Server.recruitingarticle.service;

import Moyoung.Server.recruitingarticle.repository.RecruitingArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitingArticleService {
    private final RecruitingArticleRepository recruitingArticleRepository;
}
