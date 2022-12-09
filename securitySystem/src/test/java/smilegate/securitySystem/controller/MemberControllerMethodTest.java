package smilegate.securitySystem.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MemberControllerMethodTest {

    @Test
    void nameErrorCheck() {

    }

    @Test
    void phoneNumberErrorCheck() {
    }

    @Test
    void passwordTest() {
        String text1 = "@Qwe12345";
        String text2 = "td12345";
        String text3 = "@12345";
        String text4 = "@qwer";

        String rex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
        boolean result1 = Pattern.matches(rex, text1);
        boolean result2 = Pattern.matches(rex, text2);
        boolean result3 = Pattern.matches(rex, text3);
        boolean result4 = Pattern.matches(rex, text4);

        Assertions.assertThat(result1).isEqualTo(true);
        Assertions.assertThat(result2).isEqualTo(false);
        Assertions.assertThat(result3).isEqualTo(false);
        Assertions.assertThat(result4).isEqualTo(false);
    }

    @Test
    void isValidPassword() {
        String password = "@qwer1234";

        String test = "Pass";

        // 최소 8자, 최대 20자 상수 선언
        final int MIN = 8;
        final int MAX = 20;

        // 영어, 숫자, 특수문자 포함한 MIN to MAX 글자 정규식
        final String REGEX =
                "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
        // 3자리 연속 문자 정규식
        final String SAMEPT = "(\\w)\\1\\1";
        // 공백 문자 정규식
        final String BLANKPT = "(\\s)";

        // 정규식 검사객체
        Matcher matcher;

        // 공백 체크
        if (password == null || "".equals(password)) {
            test= "Detected: No Password";
        }

        // ASCII 문자 비교를 위한 UpperCase
        String tmpPw = password.toUpperCase();
        // 문자열 길이
        int strLen = tmpPw.length();

        // 글자 길이 체크
        if (strLen > 20 || strLen < 8) {
            test= "Detected: Incorrect Length(Length: " + strLen + ")";
        }

        // 공백 체크
        matcher = Pattern.compile(BLANKPT).matcher(tmpPw);
        if (matcher.find()) {
            test= "Detected: Blank";
        }

        // 비밀번호 정규식 체크
        matcher = Pattern.compile(REGEX).matcher(tmpPw);
        if (!matcher.find()) {
            test= "Detected: Wrong Regex";
        }

        // 동일한 문자 3개 이상 체크
        matcher = Pattern.compile(SAMEPT).matcher(tmpPw);
        if (matcher.find()) {
            test= "Detected: Same Word";
        }

        // ASCII Char를 담을 배열 선언
        int[] tmpArray = new int[strLen];

        // Make Array
        for (int i = 0; i < strLen; i++) {
            tmpArray[i] = tmpPw.charAt(i);
        }

        // Validation Array
        for (int i = 0; i < strLen - 2; i++) {
            // 첫 글자 A-Z / 0-9
            if ((tmpArray[i] > 47
                    && tmpArray[i + 2] < 58)
                    || (tmpArray[i] > 64
                    && tmpArray[i + 2] < 91)) {
                // 배열의 연속된 수 검사
                // 3번째 글자 - 2번째 글자 = 1, 3번째 글자 - 1번째 글자 = 2
                if (Math.abs(tmpArray[i + 2] - tmpArray[i + 1]) == 1
                        && Math.abs(tmpArray[i + 2] - tmpArray[i]) == 2) {
                    char c1 = (char) tmpArray[i];
                    char c2 = (char) tmpArray[i + 1];
                    char c3 = (char) tmpArray[i + 2];
                    test = "Detected: Continuous Pattern: \"" + c1 + c2 + c3 + "\"";
                }
            }
        }

        Assertions.assertThat(test).isEqualTo("Pass");
    }

    @Test
    void stringContainCheck() {
    }
}