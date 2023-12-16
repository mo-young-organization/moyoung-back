package Moyoung.Server.recruitingarticle.mapper;

import Moyoung.Server.chat.entity.ChatRoomInfo;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.dto.RecruitingArticleDto;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.runningtime.entity.RunningTime;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

        requestBody.getAges()
                .forEach(age -> {
                    switch (age) {
                        case 1:
                            recruitingArticle.addAge(RecruitingArticle.Age.TEENAGER);
                            break;
                        case 2:
                            recruitingArticle.addAge(RecruitingArticle.Age.TWENTIES);
                            break;
                        case 3:
                            recruitingArticle.addAge(RecruitingArticle.Age.THIRTIES);
                            break;
                    }
                });

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

        requestBody.getAges()
                .forEach(age -> {
                    switch (age) {
                        case 1:
                            recruitingArticle.addAge(RecruitingArticle.Age.TEENAGER);
                            break;
                        case 2:
                            recruitingArticle.addAge(RecruitingArticle.Age.TWENTIES);
                            break;
                        case 3:
                            recruitingArticle.addAge(RecruitingArticle.Age.THIRTIES);
                            break;
                    }
                });

        return recruitingArticle;
    }

    default List<RecruitingArticleDto.ResponseForList> recruitingArticlesToList(List<RecruitingArticle> recruitingArticles) {
        return recruitingArticles.stream()
                .map(recruitingArticle -> recruitingArticleToResponseForList(recruitingArticle))
                .collect(toList());
    }

    default RecruitingArticleDto.ResponseForList recruitingArticleToResponseForList(RecruitingArticle recruitingArticle) {
        Member writer = recruitingArticle.getMember();
        RunningTime runningTime = recruitingArticle.getRunningTime();
        return RecruitingArticleDto.ResponseForList.builder()
                .recruitingArticleId(recruitingArticle.getRecruitingArticleId())
                .writerDisplayName(writer.getDisplayName())
                .writerAge(writer.getAge().getAge())
                .writerGender(writer.getGender() ? "남성" : "여성")
                .title(recruitingArticle.getTitle())
                .cinemaRegion(recruitingArticle.getCinemaRegion())
                .cinemaName(recruitingArticle.getCinemaName())
                .cinemaBrand(recruitingArticle.getCinemaBrand())
                .movieThumbnailUrl(recruitingArticle.getMovieThumbnailUrl())
                .movieName(recruitingArticle.getMovieName())
                .movieRating(recruitingArticle.getMovieRating())
                .startTime(runningTime.getStartTime())
                .screenInfo(runningTime.getScreenInfo())
                .maxNum(recruitingArticle.getMaxNum())
                .currentNum(recruitingArticle.getCurrentNum())
                .gender(recruitingArticle.getGender().getExplain())
                .ages(recruitingArticle.getAges().stream().map(age -> age.getAge()).collect(toList())).build();
    }

    default RecruitingArticleDto.Response recruitingArticleToResponse(RecruitingArticle recruitingArticle) {
        Member writer = recruitingArticle.getMember();
        RunningTime runningTime = recruitingArticle.getRunningTime();
        return RecruitingArticleDto.Response.builder()
                .recruitingArticleId(recruitingArticle.getRecruitingArticleId())
                .writerMemberId(writer.getMemberId())
                .writerDisplayName(writer.getDisplayName())
                .writerAge(writer.getAge().getAge())
                .writerGender(writer.getGender() ? "남성" : "여성")
                .title(recruitingArticle.getTitle())
                .cinemaRegion(recruitingArticle.getCinemaRegion())
                .cinemaName(recruitingArticle.getCinemaName())
                .cinemaBrand(recruitingArticle.getCinemaBrand())
                .movieThumbnailUrl(recruitingArticle.getMovieThumbnailUrl())
                .movieName(recruitingArticle.getMovieName())
                .movieRating(recruitingArticle.getMovieRating())
                .startTime(runningTime.getStartTime())
                .screenInfo(runningTime.getScreenInfo())
                .maxNum(recruitingArticle.getMaxNum())
                .currentNum(recruitingArticle.getCurrentNum())
                .gender(recruitingArticle.getGender().getExplain())
                .ages(recruitingArticle.getAges().stream().map(age -> age.getAge()).collect(toList()))
                .userInfos(chatRoomInfoListToUserInfoList(recruitingArticle.getChatRoomInfos())).build();
    }

    default List<RecruitingArticleDto.UserInfo> chatRoomInfoListToUserInfoList(List<ChatRoomInfo> chatRoomInfoList) {
        return chatRoomInfoList.stream()
                .map(chatRoomInfo -> chatRoomInfoToUserInfo(chatRoomInfo))
                .collect(toList());
    }

    default RecruitingArticleDto.UserInfo chatRoomInfoToUserInfo(ChatRoomInfo chatRoomInfo) {
        Member member = chatRoomInfo.getMember();
        return RecruitingArticleDto.UserInfo.builder()
                .memberId(member.getMemberId())
                .displayName(member.getDisplayName())
                .age(member.getAge().getAge())
                .gender(member.getGender() ? "남성" : "여성").build();
    }
}
