package Moyoung.Server.recruitingarticle.mapper;

import Moyoung.Server.cinema.dto.CinemaDto;
import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.movie.dto.MovieDto;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.runningtime.dto.RunningTimeDto;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecruitingArticleMapper {
    default RecruitingArticle postToRecruitingArticle(RecruitingArticleDto.PostPatch requestBody) {
        RunningTime runningTime = new RunningTime();
        runningTime.setRunningTimeId(requestBody.getRunningTimeId());
        RecruitingArticle recruitingArticle = new RecruitingArticle();
        recruitingArticle.setRunningTime(runningTime);
        recruitingArticle.setTitle(requestBody.getTitle());
        recruitingArticle.setMaxNum(requestBody.getMaxNum());
        recruitingArticle.setCreatedAt(LocalDateTime.now());
        int gender = requestBody.getGender();
        int age = requestBody.getAge();
        switch (gender) {
            case 1:
                recruitingArticle.setGender(RecruitingArticle.Gender.ALL);
                break;
            case 2:
                recruitingArticle.setGender(RecruitingArticle.Gender.MAN);
                break;
            case 3:
                recruitingArticle.setGender(RecruitingArticle.Gender.WOMAN);
                break;
        }
        switch (age) {
            case 1:
                recruitingArticle.setAge(RecruitingArticle.Age.TEENAGER);
                break;
            case 2:
                recruitingArticle.setAge(RecruitingArticle.Age.TWENTIES);
                break;
            case 3:
                recruitingArticle.setAge(RecruitingArticle.Age.THIRTIES);
                break;
        }
        return recruitingArticle;
    }

    default RecruitingArticle patchToRecruitingArticle(RecruitingArticleDto.PostPatch requestBody, long recruitingArticleId) {
        RunningTime runningTime = new RunningTime();
        runningTime.setRunningTimeId(requestBody.getRunningTimeId());
        RecruitingArticle recruitingArticle = new RecruitingArticle();
        recruitingArticle.setRecruitingArticleId(recruitingArticleId);
        recruitingArticle.setTitle(requestBody.getTitle());
        recruitingArticle.setMaxNum(requestBody.getMaxNum());
        int gender = requestBody.getGender();
        int age = requestBody.getAge();
        switch (gender) {
            case 1:
                recruitingArticle.setGender(RecruitingArticle.Gender.ALL);
                break;
            case 2:
                recruitingArticle.setGender(RecruitingArticle.Gender.MAN);
                break;
            case 3:
                recruitingArticle.setGender(RecruitingArticle.Gender.WOMAN);
                break;
        }
        switch (age) {
            case 1:
                recruitingArticle.setAge(RecruitingArticle.Age.TEENAGER);
                break;
            case 2:
                recruitingArticle.setAge(RecruitingArticle.Age.TWENTIES);
                break;
            case 3:
                recruitingArticle.setAge(RecruitingArticle.Age.THIRTIES);
                break;
        }
        return recruitingArticle;
    }

    default List<RecruitingArticleDto.ResponseForList> recruitingArticlesToList(List<RecruitingArticle> recruitingArticles) {
        return recruitingArticles.stream()
                .map(recruitingArticle -> recruitingArticleToReseponseForList(recruitingArticle))
                .collect(Collectors.toList());
    }

    default RecruitingArticleDto.ResponseForList recruitingArticleToReseponseForList(RecruitingArticle recruitingArticle) {
        RunningTime runningTime = recruitingArticle.getRunningTime();
        return RecruitingArticleDto.ResponseForList.builder()
                .recruitingArticleId(recruitingArticle.getRecruitingArticleId())
                .title(recruitingArticle.getTitle())
                .maxNum(recruitingArticle.getMaxNum())
                .currentNum(recruitingArticle.getParticipants().size())
                .gender(recruitingArticle.getGender().getExplain())
                .age(recruitingArticle.getAge().getAge())
                .runningTimeInfo(RunningTimeDto.Response.builder()
                        .runningTimeId(runningTime.getRunningTimeId())
                        .startTime(runningTime.getStartTime())
                        .endTime(runningTime.getEndTime()).build()).build();
    }
}
