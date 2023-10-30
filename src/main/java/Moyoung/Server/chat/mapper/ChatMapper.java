package Moyoung.Server.chat.mapper;

import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomInfo;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    default Chat sendToChat(ChatDto.Send requestBody, long recruitArticleId) {
        Member member = new Member();
        member.setMemberId(requestBody.getSenderId());
        RecruitingArticle recruitingArticle = new RecruitingArticle();
        recruitingArticle.setRecruitingArticleId(recruitArticleId);
        Chat chat = new Chat();
        chat.setSender(member);
        chat.setRecruitingArticle(recruitingArticle);
        chat.setChatTime(LocalDateTime.now());
        chat.setContent(requestBody.getMessage());

        return chat;
    }

    default List<ChatDto.ChatRoomResponse> chatRoomInfosToList(List<ChatRoomInfo> chatRoomInfoList) {
        return chatRoomInfoList.stream()
                .map(chatRoomInfo -> chatRoomInfoToResponse(chatRoomInfo))
                .collect(Collectors.toList());
    }

    default ChatDto.ChatRoomResponse chatRoomInfoToResponse(ChatRoomInfo chatRoomInfo) {
        RecruitingArticle recruitingArticle = chatRoomInfo.getRecruitingArticle();
        return ChatDto.ChatRoomResponse.builder()
                .recruitingArticleId(recruitingArticle.getRecruitingArticleId())
                .title(recruitingArticle.getTitle())
                .maxNum(recruitingArticle.getMaxNum())
                .currentNum(recruitingArticle.getCurrentNum())
                .movieThumbnailUrl(recruitingArticle.getMovieThumbnailUrl())
                .cinemaName(recruitingArticle.getCinemaName())
                .cinemaBrand(recruitingArticle.getCinemaBrand())
                .unreadCount(chatRoomInfo.getUnreadCount())
                .lastMessage(chatRoomInfo.getLastMessage())
                .lastMessageAt(chatRoomInfo.getLastMessageAt()).build();
    }

    default List<ChatDto.Response> chatsToList(List<Chat> chatList) {
        return chatList.stream()
                .map(chat -> chatToResponse(chat))
                .collect(Collectors.toList());
    }

    default ChatDto.Response chatToResponse(Chat chat) {
        Member sender = chat.getSender();
        String displayName = sender.getDisplayName();
        if (chat.getType().equals(Chat.Type.ENTER) || chat.getType().equals(Chat.Type.EXIT)) displayName = "[알림]";
        return ChatDto.Response.builder()
                .senderId(sender.getMemberId())
                .displayName(displayName)
                .chatTime(chat.getChatTime())
                .content(chat.getContent()).build();
    }
}
