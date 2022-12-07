package smilegate.securitySystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/join")
    public String JoinPage() {
        return "/member/join";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @PostMapping("/checkMember")
    public String checkMemberPage() {
        return "/member/checkMember";
    }

    @PostMapping("/memberList")
    public String memberViewPage() {
        return "/member/memberListView";
    }
}
