package Moyoung.Server.recruitingarticle.repository;

import Moyoung.Server.recruitingarticle.entity.QRecruitingArticle;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class RecruitingArticleRepositoryImpl implements RecruitingArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RecruitingArticle> findAllByGenderNumAndAgeInAndTitleContainingUseDistance(
            Integer genderNum,
            List<RecruitingArticle.Age> ageList,
            double x,
            double y,
            double distance,
            String keyword,
            Pageable pageable) {

        QRecruitingArticle ra = QRecruitingArticle.recruitingArticle;

        BooleanExpression genderExpression = null;

        if (genderNum != null) {
            switch (genderNum) {
                case 0 -> genderExpression = ra.gender.eq(RecruitingArticle.Gender.ALL);
                case 1 -> genderExpression = ra.gender.eq(RecruitingArticle.Gender.MAN);
                case 2 -> genderExpression = ra.gender.eq(RecruitingArticle.Gender.WOMAN);
            }
        }

        BooleanExpression ageExpression = ra.ages.any().in(ageList);

        BooleanExpression distanceExpression = ra.x.doubleValue().eq(x)
                .and(ra.y.doubleValue().eq(y))
                .and(Expressions.numberTemplate(Double.class, "FUNCTION('ST_DISTANCE_SPHERE', " +
                        "POINT({0}, {1}), POINT({2}, {3}))", ra.x, ra.y, x, y).loe(distance));

        BooleanExpression keywordExpression = null;

        if (keyword != null && !keyword.isEmpty()) {
            keywordExpression = ra.title.lower().likeIgnoreCase("%" + keyword + "%");
        }

        List<RecruitingArticle> result = queryFactory
                .selectFrom(ra)
                .where(genderExpression.and(ageExpression).and(distanceExpression).and(keywordExpression))
                .orderBy(
                        ra.x.doubleValue().eq(x).and(ra.y.doubleValue().eq(y)).asc(),
                        ra.recruitingArticleId.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(ra)
                .where(genderExpression.and(ageExpression).and(distanceExpression).and(keywordExpression))
                .fetchCount();

        return PageableExecutionUtils.getPage(result, pageable, () -> totalCount);
    }

    @Override
    public Page<RecruitingArticle> findAllByGenderNumAndAgeInAndTitleContaining(
            Integer genderNum, List<RecruitingArticle.Age> ageList,
            double x, double y, double distance, String keyword, Pageable pageable) {

        QRecruitingArticle ra = QRecruitingArticle.recruitingArticle;

        BooleanExpression genderExpression = null;
        if (genderNum != null) {
            switch (genderNum) {
                case 0:
                    genderExpression = ra.gender.eq(RecruitingArticle.Gender.ALL);
                    break;
                case 1:
                    genderExpression = ra.gender.eq(RecruitingArticle.Gender.MAN);
                    break;
                case 2:
                    genderExpression = ra.gender.eq(RecruitingArticle.Gender.WOMAN);
                    break;
            }
        }

        BooleanExpression ageExpression = ra.ages.any().in(ageList);
        BooleanExpression distanceExpression = ra.x.doubleValue().eq(x)
                .and(ra.y.doubleValue().eq(y))
                .and(Expressions.numberTemplate(Double.class, "FUNCTION('ST_DISTANCE_SPHERE', " +
                        "POINT({0}, {1}), POINT({2}, {3}))", ra.x, ra.y, x, y).loe(distance));

        BooleanExpression keywordExpression = null;

        if (keyword != null && !keyword.isEmpty()) {
            keywordExpression = ra.title.lower().likeIgnoreCase("%" + keyword + "%");
        }

        BooleanExpression finalExpression = null;

        if (genderExpression != null) {
            finalExpression = finalExpression.and(genderExpression);
        }

        finalExpression = finalExpression.and(ageExpression)
                .and(distanceExpression)
                .and(keywordExpression);

        List<RecruitingArticle> results = queryFactory
                .selectFrom(QRecruitingArticle.recruitingArticle)
                .where(finalExpression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(QRecruitingArticle.recruitingArticle)
                .where(finalExpression)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<RecruitingArticle> findAllByTitleContaining(
            String keyword, double x, double y, Pageable pageable) {

        QRecruitingArticle ra = QRecruitingArticle.recruitingArticle;

        BooleanExpression distanceExpression = ra.x.isNotNull().and(ra.y.isNotNull())
                .and(Expressions.numberTemplate(Double.class, "FUNCTION('ST_DISTANCE_SPHERE', " +
                                "POINT({0}, {1}), POINT({2}, {3}))", ra.x, ra.y, x, y).loe(3000));

        BooleanExpression keywordExpression = (keyword != null && !keyword.isEmpty()) ? ra.title.lower().likeIgnoreCase("%" + keyword + "%") : null;

        BooleanExpression finalExpression = ra.x.isNotNull().and(ra.y.isNotNull());

        finalExpression = finalExpression.and(distanceExpression)
                .and(keywordExpression);

        long total = queryFactory
                .selectFrom(ra)
                .where(finalExpression)
                .fetchCount();

        List<RecruitingArticle> results = queryFactory
                .selectFrom(ra)
                .where(finalExpression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }
}
