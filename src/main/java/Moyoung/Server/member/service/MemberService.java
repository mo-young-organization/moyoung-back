package Moyoung.Server.member.service;

import Moyoung.Server.exception.BusinessLogicException;
import Moyoung.Server.exception.ExceptionCode;
import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.member.entity.Member;
import Moyoung.Server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로컬 회원가입
    public void joinInLocal(Member member) {
        verifyExistingId(member.getId());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    // 회원 정보 등록
    public void registerInformation(Long memberId, Member requestBody) {
        verifyExistingDisplayName(requestBody.getDisplayName());
        Member member = findVerifiedMember(memberId);
        member.setDisplayName(requestBody.getDisplayName());
        member.setGender(requestBody.isGender());
        member.setAge(requestBody.getAge());
        memberRepository.save(member);
    }

    // 오어스 회원 가입
    public Member joinInOauth(Member member) {
        verifyExistingId(member.getId());

        return memberRepository.save(member);
    }

    // 오어스 로그인 중 joinInOauth 에서 MEMBER_EMAIL_EXISTS 예외 발생 시 사용 될 메서드
    public Member findMemberById(String id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    // 닉네임 중복 확인(닉네임만)
    public void checkDisplayName(MemberDto.DisplayName requestBody) {
        verifyExistingDisplayName(requestBody.getDisplayName());
    }

    // 로컬 회원가입 시 이메일 중복 확인
    private void verifyExistingId(String id) {
        Optional<Member> foundMemberById = memberRepository.findById(id);
        if (foundMemberById.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ID_EXISTS);
        }
    }

    // 닉네임 중복 확인
    private void verifyExistingDisplayName(String displayName) {
        Optional<Member> foundMemberByDisplayName = memberRepository.findByDisplayName(displayName);
        if (foundMemberByDisplayName.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ID_EXISTS);
        }
    }

    public Member findVerifiedMember (Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findedMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return findedMember;
    }
}
