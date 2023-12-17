package Moyoung.Server.recruitingarticle.repository;

import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitingArticleRepositoryCustom {
    Page<RecruitingArticle> findAllByGenderNumAndAgeInAndTitleContainingUseDistance(
            Integer genderNum,
            List<RecruitingArticle.Age> ageList,
            double x,
            double y,
            double distance,
            String keyword,
            Pageable pageable
    );

    Page<RecruitingArticle> findAllByGenderNumAndAgeInAndTitleContaining(
            Integer genderNum,
            List<RecruitingArticle.Age> ageList,
            double x,
            double y,
            double distance,
            String keyword,
            Pageable pageable
    );

    Page<RecruitingArticle> findAllByTitleContaining(
            String keyword,
            double x,
            double y,
            Pageable pageable
    );
}
