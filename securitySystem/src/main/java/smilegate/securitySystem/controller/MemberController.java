package smilegate.securitySystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String JoinPage() {
        return "/member/join";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String loginExecute(Model model) {
        return "index";
    }

    @PostMapping("/checkMember")
    public String checkMemberPage() {

        return "/member/checkMember";
    }

    @GetMapping("/memberList")
    public String memberViewPage(Model model) {
        List<Member> allMember = memberRepository.findAll();
        model.addAttribute("members", allMember);
        return "/member/memberListView";
    }

    @PostConstruct
    public void tempMember() {
        memberRepository.save(new Member("성준", "tjdwns1@email.com", "1234", "4537"));
        memberRepository.save(new Member("정각", "jun@email.com", "2345", "1234"));
        memberRepository.save(new Member("숭어", "aksk@email.com", "3456", "2345"));
        memberRepository.save(new Member("성준", "tjdwns2@email.com", "1234", "4537"));
    }
}
