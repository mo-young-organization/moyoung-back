package Moyoung.Server.recruitingarticle.service;

import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomMembersInfo;
import Moyoung.Server.chat.repository.ChatRepository;
import Moyoung.Server.chat.repository.ChatRoomMembersInfoRepository;
import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.service.MemberService;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitingArticleService {
    private final RecruitingArticleRepository recruitingArticleRepository;
    private final MemberService memberService;
    private final RunningTimeService runningTimeService;
    private final ChatRepository chatRepository;
    private final ChatRoomMembersInfoRepository chatRoomMembersInfoRepository;
    private final SimpMessageSendingOperations sendingOperations;

    // 게시글 등록
    public void registerRecruitingArticle(RecruitingArticle recruitingArticle, long memberId) {
        Member member = memberService.findVerifiedMember(memberId);
        RunningTime runningTime = runningTimeService.findVerifiedRunningTime(recruitingArticle.getRunningTime().getRunningTimeId());
        recruitingArticle.setMember(member);
        recruitingArticle.setRunningTime(runningTime);
        recruitingArticleRepository.save(recruitingArticle);
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

    // 게시글 리스트
    public Page<RecruitingArticle> getRecruitingArticleList(int page) {
        return recruitingArticleRepository.findAll(PageRequest.of(page - 1, 20, Sort.by("recruitingArticleId").descending()));
    }

    // 게시글 삭제
    public void deleteRecruitingArticle(long recruitingArticleId, long memberId) {
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(recruitingArticleId);

        checkAuthor(memberId, recruitingArticle.getMember().getMemberId());

        recruitingArticleRepository.delete(recruitingArticle);
    }

    // 게시글 참가 (발신자 확인을 위해 닉네임 반환)
    public List<ChatRoomMembersInfo> enterRecruit(long recruitingArticleId, long memberId) {
        RecruitingArticle recruitingArticle = findVerifiedRecruitingArticle(recruitingArticleId);
        Member member = memberService.findVerifiedMember(memberId);

        if (chatRoomMembersInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(memberId, recruitingArticleId).isEmpty()) {
            if (recruitingArticle.getMaxNum() == recruitingArticle.getCurrentNum()) {
                throw new BusinessLogicException(ExceptionCode.CAN_NOT_ENTER);
            }

            recruitingArticle.setCurrentNum(recruitingArticle.getCurrentNum() + 1);
            recruitingArticleRepository.save(recruitingArticle);
            List<Chat> chats = chatRepository.findByRecruitingArticleRecruitingArticleId(recruitingArticleId);
            ChatRoomMembersInfo chatRoomMembersInfo = new ChatRoomMembersInfo();
            chatRoomMembersInfo.setMember(member);
            chatRoomMembersInfo.setRecruitingArticle(recruitingArticle);
            chatRoomMembersInfo.setUnreadMessageCount(0);
            if (chats.size() > 0) {
                chatRoomMembersInfo.setLastMessageId(chats.get(chats.size() - 1).getChatId());
            } else {
                chatRoomMembersInfo.setLastMessageId(0L);
            }
            chatRoomMembersInfoRepository.save(chatRoomMembersInfo);
            recruitingArticle.setParticipants(chatRoomMembersInfoRepository.findByRecruitingArticleRecruitingArticleId(recruitingArticleId));

            Chat chat = new Chat();
            chat.setSender(member.getDisplayName());
            chat.setRecruitingArticle(recruitingArticle);
            chat.setType(Chat.ContentType.ENTER);
            chat.setContent("[알림] " + member.getDisplayName() + "님이 입장하셨습니다.");

            Chat savedChat = chatRepository.save(chat);
            sendingOperations.convertAndSend("/receive/chat/" + recruitingArticleId, savedChat);

            return chatRoomMembersInfoRepository.findByRecruitingArticleRecruitingArticleId(recruitingArticleId);
        } else if (chatRoomMembersInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(memberId, recruitingArticleId).isPresent()) {
            List<Chat> chatList = chatRepository.findByRecruitingArticleRecruitingArticleId(recruitingArticleId);
            ChatRoomMembersInfo chatRoomMembersInfo = chatRoomMembersInfoRepository.findByMemberMemberIdAndRecruitingArticleRecruitingArticleId(memberId, recruitingArticleId).get();
            Long count = chatRoomMembersInfo.getLastMessageId();

            for (int i = 0; i < chatList.size(); i++) {
                Chat currentChat = chatList.get(i);
                if (currentChat.getChatId() > count) {
                    if (currentChat.getUnreadCount() != 0) {
                        currentChat.setUnreadCount(currentChat.getUnreadCount() - 1);
                        chatRepository.save(currentChat);
                    }
                }
                if (i == chatList.size() - 1) {
                    chatRoomMembersInfo.setLastMessageId(currentChat.getChatId());
                }
            }
            chatRoomMembersInfoRepository.save(chatRoomMembersInfo);
            sendingOperations.convertAndSend("/receive/chat/" + recruitingArticleId, Chat.builder()
                    .sender(member.getDisplayName())
                    .content("")
                    .type(Chat.ContentType.REENTER)
                    .build());
            return chatRoomMembersInfoRepository.findByRecruitingArticleRecruitingArticleId(recruitingArticleId);
        } else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
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
