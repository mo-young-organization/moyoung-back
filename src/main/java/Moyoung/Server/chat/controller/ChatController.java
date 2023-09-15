package Moyoung.Server.chat.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.entity.Chat;
import Moyoung.Server.chat.mapper.ChatMapper;
import Moyoung.Server.chat.service.ChatService;
import Moyoung.Server.chat.service.WebSocketSessionService;
import Moyoung.Server.dto.MultiResponseDto;
import Moyoung.Server.recruitingarticle.service.RecruitingArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final RecruitingArticleService recruitingArticleService;
    private final ChatMapper chatMapper;
    private final WebSocketSessionService sessionService;
    private final SimpMessageSendingOperations operations;


    @MessageMapping("/{recruit-id}/chat/enter")
    public void enterMember(@PathVariable("recruit-id") long recruitId, SimpMessageHeaderAccessor headerAccessor) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();

        String sender = recruitingArticleService.enterRecruit(recruitId, authenticationMemberId);
        String recruitArticleId = String.valueOf(recruitId);
        String sessionId = headerAccessor.getSessionId();
        sessionService.registerSession(sessionId, sender, recruitArticleId);

        ChatDto.Enter enterChat = new ChatDto.Enter();
        enterChat.setContent(sender + "님이 입장하였습니다.");
        enterChat.setSender(sender);

        operations.convertAndSend("/sub/recruit/" + recruitId + "/chat", enterChat);
    }

    @PostMapping("/recruit/{recruit-id}/chat")
    public ResponseEntity postChat(@PathVariable("recruit-id") long recruitArticleId,
                                   @RequestBody ChatDto.Post requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();

        chatService.SendChat(chatMapper.postToChat(requestBody, authenticationMemberId, recruitArticleId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/recruit/{recruit-id}/chat")
    public ResponseEntity getChat(@PathVariable("recruit-id") long recruitArticleId,
                                  @RequestParam int page){
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        Page<Chat> pageChat = chatService.loadChat(recruitArticleId, authenticationMemberId, page);
        List<Chat> chatList = pageChat.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(chatMapper.chatsToList(chatList), pageChat), HttpStatus.OK);
    }
}
