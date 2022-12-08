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
}
