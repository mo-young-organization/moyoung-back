package Moyoung.Server.runningtime.repository;

import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.entity.QMovie;
import Moyoung.Server.runningtime.entity.QRunningTime;
import Moyoung.Server.runningtime.entity.RunningTime;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class RunningTimeRepositoryImpl implements RunningTimeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RunningTime> findRunningTimesByCinemaAndStartTimeBetweenAndMovieNameContaining(
            Cinema cinema, LocalDateTime startOfDate, LocalDateTime endOfDate, String movieName) {

        QRunningTime rt = QRunningTime.runningTime;
        QMovie m = QMovie.movie;

        BooleanExpression result = rt.cinema.eq(cinema)
                .and(rt.startTime.gt(startOfDate))
                .and(rt.startTime.lt(endOfDate));

        if (movieName != null && !movieName.isEmpty()) {
            result = result.and(rt.movie.in(
                    JPAExpressions.select(m)
                            .from(m)
                            .where(m.name.like("%" + movieName + "%"))
            ));
        }

        return queryFactory
                .selectFrom(rt)
                .where(result)
                .fetch();
    }

    @Override
    public List<RunningTime> findRunningTimesByCinemaAndMovieAndStartTimeBetween(
            Cinema cinema, Movie movie, LocalDateTime startOfDate, LocalDateTime endOfDate) {

        QRunningTime rt = QRunningTime.runningTime;

        BooleanExpression predicate = rt.cinema.eq(cinema)
                .and(rt.movie.eq(movie))
                .and(rt.startTime.between(startOfDate, endOfDate));

        return queryFactory
                .selectFrom(rt)
                .where(predicate)
                .fetch();
    }

    @Override
    public void deleteAllByRecruitingArticlesIsEmptyAndStartTimeBefore(LocalDateTime localDateTime) {
        QRunningTime rt = QRunningTime.runningTime;

        queryFactory
                .delete(rt)
                .where(rt.recruitingArticles.isEmpty().and(rt.startTime.before(localDateTime)))
                .execute();
    }
}
