package Moyoung.Server.movie.repository;

import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.movie.entity.QMovie;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Movie> findByName(String name) {
        QMovie movie = QMovie.movie;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(movie)
                        .where(movie.name.eq(name))
                        .fetchOne()
        );
    }
    public List<Movie> findAllByNameContains(String name) {
        QMovie movie = QMovie.movie;

        BooleanExpression expression = movie.name.containsIgnoreCase(name);

        return queryFactory
                .selectFrom(movie)
                .where(expression)
                .fetch();
    }
}
