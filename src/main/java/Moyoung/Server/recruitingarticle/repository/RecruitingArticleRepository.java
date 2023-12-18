package Moyoung.Server.recruitingarticle.repository;

import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitingArticleRepository extends JpaRepository<RecruitingArticle, Long> , RecruitingArticleRepositoryCustom {
}
