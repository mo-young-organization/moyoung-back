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
            "(:genderNum = 0 AND ra.gender = 0) OR " +
            "(:genderNum = 1 AND ra.gender = 1) OR " +
            "(:genderNum = 2 AND ra.gender = 2)) " +
            "AND " +
            "(ra.age IN :ageList) " +
            "AND " +
            "ST_DISTANCE_SPHERE(POINT(ra.x, ra.y), POINT(:x, :y)) <= :distance " +
            "AND " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(ra.title) LIKE %:keyword%) " +
            "ORDER BY ST_DISTANCE_SPHERE(POINT(ra.x, ra.y), POINT(:x, :y)), ra.recruitingArticleId DESC")
    Page<RecruitingArticle> findAllByGenderNumAndAgeInAndTitleContainingUseDistance(
            @Param("genderNum") Integer genderNum,
            @Param("ageList") List<RecruitingArticle.Age> ageList,
            @Param("x") double x,
            @Param("y") double y,
            @Param("distance") double distance,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT ra FROM RecruitingArticle ra WHERE " +
            "(:genderNum IS NULL OR " +
            "(:genderNum = 0 AND ra.gender = 0) OR " +
            "(:genderNum = 1 AND ra.gender = 1) OR " +
            "(:genderNum = 2 AND ra.gender = 2)) " +
            "AND " +
            "(ra.age IN :ageList) " +
            "AND " +
            "ST_DISTANCE_SPHERE(POINT(ra.x, ra.y), POINT(:x, :y)) <= :distance " +
            "AND " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(ra.title) LIKE %:keyword%)")
    Page<RecruitingArticle> findAllByGenderNumAndAgeInAndTitleContaining(
            @Param("genderNum") Integer genderNum,
            @Param("ageList") List<RecruitingArticle.Age> ageList,
            @Param("x") double x,
            @Param("y") double y,
            @Param("distance") double distance,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT ra FROM RecruitingArticle ra WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(ra.title) LIKE %:keyword%)")
    Page<RecruitingArticle> findAllByTitleContaining(@Param("keyword") String keyword, Pageable pageable);
}
