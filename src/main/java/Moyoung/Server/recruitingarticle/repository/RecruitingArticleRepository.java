package Moyoung.Server.recruitingarticle.repository;

import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitingArticleRepository extends JpaRepository<RecruitingArticle, Long> {
    @Query("SELECT ra FROM RecruitingArticle ra WHERE " +
            "(:genderNum IS NULL OR " +
            "(:genderNum = 0 AND ra.gender = '전체') OR " +
            "(:genderNum = 1 AND ra.gender = '남자만') OR " +
            "(:genderNum = 2 AND ra.gender = '여자만')) " +
            "AND " +
            "(ra.age IN :ageList)")
    Page<RecruitingArticle> findAllByGenderNumAndAgeIn(
            @Param("genderNum") Integer genderNum,
            @Param("ageList") List<RecruitingArticle.Age> ageList,
            Pageable pageable
    );
}
