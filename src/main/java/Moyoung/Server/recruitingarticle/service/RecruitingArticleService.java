package Moyoung.Server.recruitingarticle.service;

import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomInfo;
import Moyoung.Server.chat.mapper.ChatMapper;
import Moyoung.Server.chat.repository.ChatRoomInfoRepository;
import Moyoung.Server.chat.service.ChatService;
import Moyoung.Server.cinema.entity.Cinema;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
import Moyoung.Server.movie.entity.Movie;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import Moyoung.Server.recruitingarticle.repository.RecruitingArticleRepository;
import Moyoung.Server.runningtime.entity.RunningTime;
import Moyoung.Server.runningtime.service.RunningTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitingArticleService  {
    private final RecruitingArticleRepository recruitingArticleRepository;
    private final MemberService memberService;
    private final RunningTimeService runningTimeService;
    private final ChatService chatService;
    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatMapper chatMapper;
    private final SimpMessageSendingOperations operations;

    // 게시글 등록
    public void registerRecruitingArticle(RecruitingArticle recruitingArticle, long memberId) {
        Member member = memberService.findVerifiedMember(memberId);
        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(recruitingArticle.getRunningTime().getRunningTimeId());
        ChatRoomInfo chatRoomInfo = ChatRoomInfo.builder().member(member).recruitingArticle(recruitingArticle).entryTime(LocalDateTime.now()).lastMessageAt(LocalDateTime.now()).build();
        Movie movie = runningTime.getMovie();
        Cinema cinema = runningTime.getCinema();
        recruitingArticle.setMember(member);
        recruitingArticle.addChatRoomInfo(chatRoomInfo);
        recruitingArticle.setRunningTime(runningTime);
        recruitingArticle.setCinemaRegion(cinema.getRegion_1());
        recruitingArticle.setCinemaName(cinema.getName());
        recruitingArticle.setCinemaBrand(cinema.getBrand());
        recruitingArticle.setX(cinema.getX());
        recruitingArticle.setY(cinema.getY());
        recruitingArticle.setMovieName(movie.getName());
        recruitingArticle.setMovieThumbnailUrl(movie.getThumbnailUrl());
        recruitingArticle.setMovieRating(movie.getMovieRating());
        recruitingArticle.setCurrentNum(1);

        recruitingArticleRepository.save(recruitingArticle);
        chatRoomInfoRepository.save(chatRoomInfo);
    }

    // 게시글 수정
    public void updateRecruitingArticle(RecruitingArticle recruitingArticle, long memberId) {
        RecruitingArticle findedRecruitingArticle = findVerifiedRecruitingArticle(recruitingArticle.getRecruitingArticleId());

        checkAuthor(memberId, findedRecruitingArticle.getMember().getMemberId());

        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(recruitingArticle.getRunningTime().getRunningTimeId());
        findedRecruitingArticle.setTitle(recruitingArticle.getTitle());
        findedRecruitingArticle.setRunningTime(runningTime);
        findedRecruitingArticle.setMaxNum(recruitingArticle.getMaxNum());
        findedRecruitingArticle.setGender(recruitingArticle.getGender());
        findedRecruitingArticle.setAge(recruitingArticle.getAge());
        findedRecruitingArticle.setModifiedAt(LocalDateTime.now());
        recruitingArticleRepository.save(findedRecruitingArticle);
    }

    // 게시글 리스트 (비로그인)
    public Page<RecruitingArticle> getRecruitingArticleListNonLogin(int page, String keyword) {
        return recruitingArticleRepository.findAllByTitleContaining(keyword, PageRequest.of(page - 1, 20, Sort.by("recruitingArticleId").descending()));
    }

    // 게시글 리스트
    public Page<RecruitingArticle> getRecruitingArticleList(int page, Integer genderNum, Boolean teenager, Boolean twenties, Boolean thirties, double x, double y, Double distance, String keyword, Boolean sort) {
        List<RecruitingArticle.Age> ageList = new ArrayList<>();
        if (teenager != null && teenager) ageList.add(RecruitingArticle.Age.TEENAGER);
        if (twenties != null && twenties) ageList.add(RecruitingArticle.Age.TWENTIES);
        if (thirties != null && thirties) ageList.add(RecruitingArticle.Age.THIRTIES);

        if (ageList.isEmpty()) {
            ageList.add(RecruitingArticle.Age.TEENAGER);
            ageList.add(RecruitingArticle.Age.TWENTIES);
            ageList.add(RecruitingArticle.Age.THIRTIES);
        }

        if (sort == null || !sort) {
            return recruitingArticleRepository.findAllByGenderNumAndAgeInAndTitleContaining(genderNum, ageList, x, y, distance, keyword, PageRequest.of(page - 1, 20, Sort.by("recruitingArticleId").descending()));
        }
        return recruitingArticleRepository.findAllByGenderNumAndAgeInAndTitleContainingUseDistance(genderNum, ageList, x, y, distance, keyword, PageRequest.of(page - 1, 20));
    }

    // 게시글 삭제
    public void deleteRecruitingArticle(long recruitingArticleId, long memberId) {
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(recruitingArticleId);

        checkAuthor(memberId, recruitingArticle.getMember().getMemberId());

        recruitingArticleRepository.delete(recruitingArticle);
    }

    // 게시글 참가
    public void enterRecruit(long recruitingArticleId, long memberId) {
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(recruitingArticleId);
        Member member = memberService.findVerifiedMember(memberId);

        if (recruitingArticle.getMaxNum() - recruitingArticle.getCurrentNum() < 1) {
            throw new BusinessLogicException(ExceptionCode.CAN_NOT_ENTER);
        }

        // 중복 검사: 이미 멤버가 채팅방에 있는지 확인
        if (chatRoomInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(memberId, recruitingArticleId).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_ENTERED);
        }
        ChatRoomInfo chatRoomInfo = ChatRoomInfo.builder().member(member).recruitingArticle(recruitingArticle).entryTime(LocalDateTime.now()).build();
        recruitingArticle.addChatRoomInfo(chatRoomInfo);
        recruitingArticle.setCurrentNum(recruitingArticle.getCurrentNum() + 1);

        recruitingArticleRepository.save(recruitingArticle);
        chatRoomInfoRepository.save(chatRoomInfo);

        Chat chat = chatService.saveChat(Chat.builder()
                .chatTime(LocalDateTime.now())
                .type(Chat.Type.ENTER)
                .content(member.getDisplayName() + " 님이 입장했습니다.")
                .sender(member)
                .recruitingArticle(recruitingArticle).build());

        operations.convertAndSend("/sub/chatroom/" + recruitingArticleId, chatMapper.chatToResponse(chat));
    }

    // 게시글 퇴장
    public void leaveSession(String recruitingArticleId, String senderDisplayName) {
        long longRecruitingArticleId = Long.parseLong(recruitingArticleId);
        RecruitingArticle foundRecruitingArticle = findVerifiedRecruitingArticle(longRecruitingArticleId);
        Member member = memberService.findMemberByDisplayName(senderDisplayName);
        foundRecruitingArticle.setCurrentNum(foundRecruitingArticle.getCurrentNum() - 1);

        recruitingArticleRepository.save(foundRecruitingArticle);
    }

    // 검증된 게시글 찾기
    public RecruitingArticle findVerifiedRecruitingArticle(long recruitingArticleId) {
        Optional<RecruitingArticle> optionalRecruitingArticle = recruitingArticleRepository.findById(recruitingArticleId);
        RecruitingArticle recruitingArticle = optionalRecruitingArticle.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.RECRUIT_ARTICLE_NOT_FOUND));

        return recruitingArticle;
    }

    // 작성자 확인
    private static void checkAuthor(long memberId, long authorId) {
        if (authorId != memberId) {
            throw new BusinessLogicException(ExceptionCode.ONLY_AUTHOR);
        }
    }
}
