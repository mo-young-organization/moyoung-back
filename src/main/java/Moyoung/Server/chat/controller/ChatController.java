package Moyoung.Server.chat.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.entity.ChatRoomInfo;
import Moyoung.Server.chat.mapper.ChatMapper;
import Moyoung.Server.chat.service.ChatService;
import Moyoung.Server.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final SimpMessageSendingOperations operations;


    // 메세지 전송
    // PathVariable 대신 DestinationVariable 사용 (PathVariable 활용하면 파싱 불가)
    @MessageMapping("/chatroom/{recruit-id}")
    public void sendMessage(@DestinationVariable("recruit-id") long recruitArticleId, ChatDto.Send chat) {
        Chat savedChat = chatService.saveChat(chatMapper.sendToChat(chat, recruitArticleId));

        log.info("chat {} send by {} to room number{}", savedChat.getContent(), savedChat.getSender().getMemberId(), savedChat.getRecruitingArticle().getRecruitingArticleId());
        // /sub/chatroom/{id}로 메세지 보냄
        operations.convertAndSend("/sub/chatroom/" + savedChat.getRecruitingArticle().getRecruitingArticleId(), chatMapper.chatToResponse(savedChat));
    }

    // 메세지 불러오기
    @GetMapping("/chatroom/{recruit-id}")
    public ResponseEntity loadMessage(@PathVariable("recruit-id") long recruitArticleId) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        List<Chat> chatList = chatService.getChatMessage(recruitArticleId, authenticationMemberId);

        return new ResponseEntity<>(chatMapper.chatsToList(chatList), HttpStatus.OK);
    }

    @GetMapping("/chatroom")
    public ResponseEntity loadChatRoomList() {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        List<ChatRoomInfo> chatRoomInfoList = chatService.getChatRoomList(authenticationMemberId);

        return new ResponseEntity<>(chatMapper.chatRoomInfosToList(chatRoomInfoList), HttpStatus.OK);
    }
}
