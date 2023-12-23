package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.MovieRank;
import Moyoung.Server.movie.entity.QMovieRank;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class MovieRankRepositoryImpl implements MovieRankRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MovieRank> findAllByDate(LocalDate date) {
        QMovieRank movieRank = QMovieRank.movieRank1;

        return queryFactory
                .selectFrom(movieRank)
                .where(movieRank.date.eq(date))
                .fetch();
    };
}
