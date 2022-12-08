package smilegate.securitySystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepositoryImp;
import smilegate.securitySystem.repository.MemberRepositoryInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    Map<String, String> error = new HashMap<>();
    MemberRepositoryInterface memberRepository = MemberRepositoryImp.getInstance();

    @GetMapping("/join")
    public String joinPage() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinExecute(@RequestParam String password2, Member member, RedirectAttributes redirectAttributes, Model model) {
        inputErrorCheck(member.getName(), member.getPassword(), member.getPhoneNumber(), password2);
        if(hasError()){
            log.info("log info:{}", error);
            model.addAttribute("error", error);
            return "/join";
        }
        memberRepository.save(member);
        redirectAttributes.addAttribute("memberId", member.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/member/" + member.getId();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String loginExecute(Model model) {
        return "/index";
    }

    @GetMapping("/{memberId}")
    public String checkMemberPage(@PathVariable Long memberId, Model model) {
        Member member = memberRepository.findById(memberId);
        model.addAttribute("member", member);
        return "/member/checkMember";
    }

    @GetMapping("/memberList")
    public String memberViewPage(Model model) {
        List<Member> allMember = memberRepository.findAll();
        model.addAttribute("members", allMember);
        return "/member/memberListView";
    }

    public boolean hasError() {
        if(!error.isEmpty()) return true;
        return false;
    }

    public void inputErrorCheck(String name, String password1, String phoneNumber, String password2) {
        if(nameErrorCheck(name)){
            error.put("nameError", "이름 규칙이 맞지 않습니다");
        }
        if (phoneNumberErrorCheck(phoneNumber)) {
            error.put("phoneNumberError", "연락처 규칙이 맞지 않습니다.");
        }
        if (passwordErrorCheck(password1, password2)) {
            error.put("passwordError", "비밀번호 규칙이 맞지 않습니다.");
        }
    }

    public boolean nameErrorCheck(String str) {
        return !StringUtils.hasText(str) || StringUtils.containsWhitespace(str) || str.length() > 12;
    }

    public boolean phoneNumberErrorCheck(String str){
        return str.contains("-") || str.length() != 11;
    }

    public boolean passwordErrorCheck(String pw1, String pw2 ){
        if(stringContainErrorCheck(pw1)) log.debug("pwContainError : {}","contain");
        if(passwordEqualError(pw1,pw2)) log.debug("pwEqualError:{}","eqaul");
        if(stringContainErrorCheck(pw1) || passwordEqualError(pw1, pw2)) return true;
        return false;
    }

    public boolean stringContainErrorCheck(String str) {
        String pattern = "^[a-zA-Z0-9]+[!@#$%^&*]+$";
        boolean result = Pattern.matches(pattern, str);
        if(!result) return true;
        return false;
    }

    public boolean passwordEqualError(String pw1,String pw2) {
        if(!pw1.equals(pw2)) return true;
        return false;
    }
}
