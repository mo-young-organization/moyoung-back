package Moyoung.Server.cinema.repository;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.cinema.entity.QCinema;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CinemaRepositoryImpl implements CinemaRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Cinema> findCinemasWithinDistanceAndFilter(double x,
                                                           double y,
                                                           double distance,
                                                           Set<String> brands) {
        QCinema cinema = QCinema.cinema;

        NumberExpression<Double> distanceSphereExpression = Expressions.numberTemplate(Double.class,
                "ST_DISTANCE_SPHERE(POINT({0}, {1}), POINT({2}, {3}))", cinema.x, cinema.y, x, y);

        BooleanExpression distancePredicate = distanceSphereExpression.loe(distance);

        BooleanExpression brandPredicate = cinema.brand.in(brands);

        return queryFactory
                .select(cinema)
                .from(cinema)
                .where(distancePredicate.and(brandPredicate))
                .orderBy(distanceSphereExpression.asc())
                .fetch();


    }

    public List<Cinema> findCinemasByWithinDistance(double x, double y, double distance) {
        QCinema cinema = QCinema.cinema;

        NumberExpression<Double> distanceSphereExpression = Expressions.numberTemplate(Double.class,
                "ST_DISTANCE_SPHERE(POINT({0}, {1}), POINT({2}, {3}))", cinema.x, cinema.y, x, y);

        BooleanExpression distancePredicate = distanceSphereExpression.loe(distance);

        return queryFactory
                .selectFrom(cinema)
                .where(distancePredicate)
                .fetch();
    }
}
