package smilegate.securitySystem.service.MemberService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryInterface;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class memberServiceImpTest {

    @Autowired
    MemberServiceInterface memberService;

    @Autowired
    MemberRepositoryInterface memberRepository;

    @Test
    void join() {
        Member member = new Member("성준", "qwe@naver.com");

        Long joinMember = memberService.join(member);

        Assertions.assertEquals(member, memberRepository.findById(joinMember));
    }

    @Test
    void validateDuplicateMember() {
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}