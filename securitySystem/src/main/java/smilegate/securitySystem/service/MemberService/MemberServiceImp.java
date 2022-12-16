package smilegate.securitySystem.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryInterface;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImp implements MemberServiceInterface {

    private final MemberRepositoryInterface memberRepository;

    @Override
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }
}
