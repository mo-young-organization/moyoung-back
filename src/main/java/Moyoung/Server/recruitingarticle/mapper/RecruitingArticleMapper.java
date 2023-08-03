package Moyoung.Server.recruitingarticle.mapper;

import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruitingArticleMapper {
    default RecruitingArticle postToRecruitingArticle(RecruitingArticleDto.Post requestBody) {
        RunningTime runningTime = new RunningTime();
        runningTime.setRunningTimeId(requestBody.getRunningTimeId());
        RecruitingArticle recruitingArticle = new RecruitingArticle();
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
}
