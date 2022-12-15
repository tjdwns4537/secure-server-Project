package smilegate.securitySystem.repository.MemberRepository;

import smilegate.securitySystem.domain.Member;

import java.util.List;

public interface MemberRepositoryInterface {

    void save(Member member);

    Member findById(Long id);

    List<Member> findAll();

    List<Member> findByName(String name);

    List<Member> findByEmail(String email);

    void clear();
}
