package Moyoung.Server.chat.mapper;

import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    default Chat sendToChat(ChatDto.Send requestBody) {
        Member member = new Member();
        member.setMemberId(requestBody.getSenderId());
        RecruitingArticle recruitingArticle = new RecruitingArticle();
        recruitingArticle.setRecruitingArticleId(requestBody.getRecruitingArticleId());
        Chat chat = new Chat();
        chat.setSender(member);
        chat.setRecruitingArticle(recruitingArticle);
        chat.setChatTime(LocalDateTime.now());
        chat.setContent(requestBody.getMessage());

        return chat;
    }

    default List<ChatDto.Response> chatsToList(List<Chat> chatList) {
        return chatList.stream()
                .map(chat -> chatToResponse(chat))
                .collect(Collectors.toList());
    }

    default ChatDto.Response chatToResponse(Chat chat) {
        Member sender = chat.getSender();
        return ChatDto.Response.builder()
                .senderId(sender.getMemberId())
                .displayName(sender.getDisplayName())
                .chatTime(chat.getChatTime())
                .content(chat.getContent()).build();
    }
}
