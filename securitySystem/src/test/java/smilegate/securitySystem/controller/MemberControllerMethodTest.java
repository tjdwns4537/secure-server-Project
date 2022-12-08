package smilegate.securitySystem.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MemberControllerMethodTest {

    @Test
    void nameErrorCheck() {

    }

    @Test
    void phoneNumberErrorCheck() {
    }

    @Test
    void passwordErrorCheck() {
        String pattern = "^[a-zA-Z0-9]+[!@#$%^&*]+$";

        boolean result1 = Pattern.matches(pattern, "@1234");
        boolean result2 = Pattern.matches(pattern, "a4@");
        boolean result3 = Pattern.matches(pattern, "!qwe1234");

        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);

        Assertions.assertThat(result1).isEqualTo(false);
        Assertions.assertThat(result2).isEqualTo(true);
        Assertions.assertThat(result3).isEqualTo(true);
    }

    @Test
    void stringContainCheck() {
    }
}