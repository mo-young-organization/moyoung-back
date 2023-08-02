package Moyoung.Server.chat.mapper;

import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.recruitingarticle.entity.RecruitingArticle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    default Chat postToChat(ChatDto.Post requestBody, long memberId, long recruitArticleId) {
        Member member = new Member();
        member.setMemberId(memberId);
        RecruitingArticle recruitingArticle = new RecruitingArticle();
        recruitingArticle.setRecruitingArticleId(recruitArticleId);
        Chat chat = new Chat();
        chat.setSender(member);
        chat.setRecruitingArticle(recruitingArticle);
        chat.setContent(requestBody.getContent());

        return chat;
    }
}
