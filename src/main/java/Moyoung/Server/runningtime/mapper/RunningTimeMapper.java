package Moyoung.Server.runningtime.mapper;

import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.runningtime.dto.RunningTimeDto;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.mapstruct.Mapper;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public interface RunningTimeMapper {
    default List<RunningTimeDto.ArticleListResponse> runningTimeToRunningTimeListResponse(RunningTime runningTime) {
        return runningTime.getRecruitingArticles().stream()
                .map(recruitingArticle -> runningTimeToArticleListElement(recruitingArticle))
                .collect(toList());
    }

    default RunningTimeDto.ArticleListResponse runningTimeToArticleListElement(RecruitingArticle recruitingArticle) {
        return RunningTimeDto.ArticleListResponse.builder()
                .recruitingArticleId(recruitingArticle.getRecruitingArticleId())
                .title(recruitingArticle.getTitle())
                .maxNum(recruitingArticle.getMaxNum())
                .currentNum(recruitingArticle.getCurrentNum())
                .gender(recruitingArticle.getGender().getExplain())
                .ages(recruitingArticle.getAges().stream().map(age -> age.getAge()).collect(toList())).build();
    }
}
