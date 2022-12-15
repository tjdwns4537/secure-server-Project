package smilegate.securitySystem.service.MemberService;

import smilegate.securitySystem.domain.Member;

import java.util.List;

public interface memberServiceInterface {
    void join();

    void validateDuplicateMember(Member member);

    public List<Member> findMembers();

    public Member findOne(Long member);
}
