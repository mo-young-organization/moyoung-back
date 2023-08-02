package Moyoung.Server.chat.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.chat.dto.ChatDto;
import Moyoung.Server.chat.mapper.ChatMapper;
import Moyoung.Server.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;

    @PostMapping("/recruit/{recruit-id}/chat")
    public ResponseEntity postChat(@PathVariable("recruit-id") long recruitArticleId,
                                   @RequestBody ChatDto.Post requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();

        chatService.SendChat(chatMapper.postToChat(requestBody, authenticationMemberId, recruitArticleId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
