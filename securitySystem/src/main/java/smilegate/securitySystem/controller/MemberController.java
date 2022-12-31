package smilegate.securitySystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smilegate.securitySystem.domain.EmailForm;
import smilegate.securitySystem.domain.LoginForm;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.domain.MemberForm;
import smilegate.securitySystem.service.EmailService.EmailServiceImp;
import smilegate.securitySystem.service.MemberService.JoinFormValidate;
import smilegate.securitySystem.service.MemberService.MemberServiceInterface;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    private final EmailServiceImp emailService;
    private final MemberServiceInterface memberService;
    private JoinFormValidate joinFormValidate;

    public MemberController(EmailServiceImp emailService, MemberServiceInterface memberService, JoinFormValidate joinFormValidate) {
        this.emailService = emailService;
        this.memberService = memberService;
        this.joinFormValidate = joinFormValidate;
    }

    String emailVerifyCode;
    boolean emailCheckFlag;
    String globalEmail;
//    Map<String, String> error = joinFormValidate.getError();

    @GetMapping("/join")
    public String joinPage(Model model) {
        emailCheckFlag = false;
        model.addAttribute("memberForm", new MemberForm());
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinExecute(
            @Valid MemberForm memberForm,BindingResult bindingResult,
            @RequestParam String password2,
            RedirectAttributes redirectAttributes,
            Model model) {

        if(bindingResult.hasErrors()){
            return "/member/join";
        }

//        error.clear();
        joinFormValidate.inputErrorCheck(memberForm.getUserId(), memberForm.getName(), memberForm.getPassword(), memberForm.getPhoneNumber(), password2);

//        if(hasError()){
//            log.info("log info:{}", error);
//            model.addAttribute("error", error);
//            return "/member/join";
//        }

        Member member = new Member();
        member.setUserId(memberForm.getUserId());
        member.setName(memberForm.getName());
        member.setPassword(memberForm.getPassword());
        member.setPhoneNumber(memberForm.getPhoneNumber());
        member.setEmail(globalEmail);

        memberService.join(member);
        redirectAttributes.addAttribute("memberId", member.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/member/" + member.getId();
    }

    @GetMapping("/login")
    public String loginPage() {
        log.info("[GET] login");
        return "/member/login";
    }

    @PostMapping("/login-do")
    public String loginExecute(LoginForm loginForm, Model model) {
        log.info("[POST] login");
        return "/main/main";
    }

    @GetMapping("/{memberId}")
    public String checkMemberPage(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "/member/checkMember";
    }

    @GetMapping("/memberList")
    public String memberViewPage(Model model) {
        List<Member> allMember = memberService.findByAll();
        model.addAttribute("members", allMember);
        return "/member/memberListView";
    }

    @GetMapping("/emailConfirm")
    public String emailVerify(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "/member/emailVerify";
    }

    @PostMapping("/emailConfirm")
    @ResponseBody
    public String emailVerify(
            @RequestParam String email,
            BindingResult bindingResult,
            Model model) throws Exception
    {
//        if(bindingResult.hasErrors()){
//            return "/member/Confirm";
//        }

        globalEmail = email;
        log.info("post email = {}", email);
        String s = emailService.sendSimpleMessage(email);
        emailVerifyCode = s;
        return "redirect:/member/join";
    }

    @PostMapping("/emailVerify")
    @ResponseBody
    public String verifyCode(@RequestParam String emailCheck, Model model) {
        log.info("Post Verify = {}", emailCheck);
        if(!emailVerifyCode.equals(emailCheck)){
            log.info("fail verify");
            emailCheckFlag = false;
//            error.put("verifyError", "인증번호가 다릅니다.");
            return "redirect:/member/emailConfirm";
        }
        else {
            log.info("success verify");
            emailCheckFlag = true;
            return "/member/join";
        }
    }

    public boolean hasError() {
//        if(!error.isEmpty()) return true;
        return false;
    }


}
