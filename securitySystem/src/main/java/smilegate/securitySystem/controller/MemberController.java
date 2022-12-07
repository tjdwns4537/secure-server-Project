package smilegate.securitySystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepositoryImp;
import smilegate.securitySystem.repository.MemberRepositoryInterface;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    MemberRepositoryInterface memberRepository = MemberRepositoryImp.getInstance();

    @GetMapping("/join")
    public String joinPage() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinExecute(
            @RequestParam String userId,
            @RequestParam String email,
            @RequestParam String password1,
            @RequestParam String userName,
            @RequestParam String phoneNumber,
            Model model
    ) {
        Member member = new Member(userName, userId, email, password1, phoneNumber);
        memberRepository.save(member);
        model.addAttribute("member", member);
        return "/member/checkMember";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String loginExecute(Model model) {
        return "/index";
    }

    @GetMapping("/checkMember")
    public String checkMemberPage() {
        return "/member/checkMember";
    }

    @GetMapping("/memberList")
    public String memberViewPage(Model model) {
        List<Member> allMember = memberRepository.findAll();
        model.addAttribute("members", allMember);
        return "/member/memberListView";
    }

//    @PostConstruct
//    public void tempMember() {
//        memberRepository.save(new Member("성준", "전사","tjdwns1@email.com", "1234", "4537"));
//        memberRepository.save(new Member("정각", "누구세요","jun@email.com", "2345", "1234"));
//        memberRepository.save(new Member("숭어", "부산","aksk@email.com", "3456", "2345"));
//        memberRepository.save(new Member("성준", "북대생","tjdwns2@email.com", "1234", "4537"));
//    }
}
