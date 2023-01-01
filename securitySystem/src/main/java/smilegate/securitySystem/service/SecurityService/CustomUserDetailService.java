package smilegate.securitySystem.service.SecurityService;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepository.JpaMemberRepository;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryInterface;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private JpaMemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(userId).get(0);

        if (member == null) {
            throw new UsernameNotFoundException("로그인 정보가 올바르지 않습니다.");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        MemberContext memberContext = new MemberContext(member, roles);

        return memberContext;
    }
}
