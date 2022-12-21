package smilegate.securitySystem.service.MemberService;

import smilegate.securitySystem.domain.Member;

import java.util.List;

public interface MemberServiceInterface {
    Long join(Member member);

    void validateDuplicateMember(Member member);

    List<Member> findByAll();

    Member findById(Long member);
}
