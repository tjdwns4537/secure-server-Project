package smilegate.securitySystem.repository;

import smilegate.securitySystem.domain.Member;

import java.util.List;

public interface MemberRepositoryInterface {

    Member save(Member member);

    Member findById(Long id);

    List<Member> findAll();

    void clear();
}
