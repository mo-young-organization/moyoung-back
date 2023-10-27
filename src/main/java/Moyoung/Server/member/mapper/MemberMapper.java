package Moyoung.Server.member.mapper;

import Moyoung.Server.member.dto.MemberDto;
import Moyoung.Server.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member postToMember(MemberDto.Post requestBody);
    default Member infoToMember(MemberDto.Info requestBody) {
        int age = requestBody.getAge();
        Member member = new Member();
        member.setDisplayName(requestBody.getDisplayName());
        member.setGender(requestBody.getGender());

        switch (age) {
            case 1:
                member.setAge(Member.Age.TEENAGER);
                break;
            case 2:
                member.setAge(Member.Age.TWENTIES);
                break;
            case 3:
                member.setAge(Member.Age.THIRTIES);
                break;
        }

        return member;
    }
}
