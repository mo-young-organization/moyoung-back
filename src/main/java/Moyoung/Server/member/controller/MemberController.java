package Moyoung.Server.member.controller;

import Moyoung.Server.auth.interceptor.JwtParseInterceptor;
import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.mapper.MemberMapper;
import Moyoung.Server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity postInformation(@Validated @RequestBody MemberDto.PostInfo requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        Member member = memberService.registerInformation(authenticationMemberId, memberMapper.postInfoToMember(requestBody));
        return new ResponseEntity<>(memberMapper.memberToInfoResponse(member), HttpStatus.OK);
    }

    @PatchMapping("/info")
    public ResponseEntity patchInformation(@Validated @RequestBody MemberDto.PatchInfo requestBody) {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        String displayName = memberService.updateInformation(authenticationMemberId, memberMapper.patchInfoToMember(requestBody));
        return new ResponseEntity<>(displayName, HttpStatus.OK);
    }

    @PostMapping("/displayname")
    public ResponseEntity postDisplayNameCheck(@Validated @RequestBody MemberDto.DisplayName requestBody) {
        // true = 사용 가능, false 사용 불가 (이미 사용 중)
        return new ResponseEntity<>(memberService.checkDisplayName(requestBody), HttpStatus.OK);
    }


    @DeleteMapping("/member")
    public ResponseEntity delete() {
        long authenticationMemberId = JwtParseInterceptor.getAuthenticatedMemberId();
        memberService.deleteMember(authenticationMemberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
