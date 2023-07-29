package Moyoung.Server.member.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.member.mapper.MemberMapper;
import Moyoung.Server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @PostMapping("/signup")
    public ResponseEntity createMember(@RequestBody MemberDto.Post requestBody) {
        memberService.joinInLocal(memberMapper.postToMember(requestBody));
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/info")
    public ResponseEntity postInformation(@RequestBody MemberDto.Info requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        memberService.registerInformation(authenticationMemberId, memberMapper.infoToMember(requestBody));
        return new ResponseEntity<>("정보 등록이 완료되었습니다.", HttpStatus.OK);
    }
}
