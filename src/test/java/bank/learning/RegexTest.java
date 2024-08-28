package bank.learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

// java.util.regex.Pattern
public class RegexTest {

    @Test
    @DisplayName("한글만 통과 하는 regex")
    void korean_success(){
        //given
        String value = "한글";
        boolean result = Pattern.matches("^[가-힣]+$", value);
        //when
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("한글이 통과 하지않는 regex")
    void korean_fail(){
        String value = "한글";
        boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
        //when
        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("영어만 통과 하는 regex")
    void english_success(){
        //given
        String value = "ssar";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        //when
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("영어가 통과 하지않는 regex")
    void english_fail(){
        String value = "ssar";
        boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
        //when
        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("영어와 숫자만 통과 하는 regex")
    void englishWithNumber_success(){
        //given
        String value = "ssar1";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        //when
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("영어만 되고 길이는 최소 2~4까지 통과하는 regex")
    void english_length_success(){
        //given
        String value = "ssar";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        //when
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("username 테스트, 영문/숫자 2~20자 이내")
    void username(){
        //given
        String username = "ssar";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        //when
        //then
    }

    @Test
    @DisplayName("fullname 테스트, 영어,한글,1~20")
    void fullname(){
        //given
        String username = "정인호";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", username);
        //when
        //then
        System.out.println(result);
    }

    @Test
    @DisplayName("fullname 테스트, 영어,한글,1~20")
    void email(){
        //given
        String username = "inho@naver.com";
        boolean result =
                Pattern.matches("^[a-zA-Z0-9]{2,6}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", username);
        //when
        //then
        System.out.println(result);
    }

    @Test
    @DisplayName("")
    void account_gubun_test(){
        //given
        String gubun = "DEPOSIT";
        boolean result =
                Pattern.matches("^(DEPOSIT)$", gubun);
        //when
        //then
        System.out.println(result);
    }

    @Test
    @DisplayName("")
    void account_gubun_test_fail(){
        //given
        String gubun = "DEPOSIT1";
        boolean result =
                Pattern.matches("^(DEPOSIT)$", gubun);
        //when
        //then
        System.out.println(result);
    }

    @Test
    @DisplayName("")
    void account_tel_test(){
        //given
        String tel = "01012345678";
        boolean result =
                Pattern.matches("^[0-9]{3}[0-9]{4}[0-9]{4}", tel);
        //when
        //then
        System.out.println(result);
    }

    @Test
    @DisplayName("")
    void account_tel_test_fail(){
        //given
        String tel = "010123456781";
        boolean result =
                Pattern.matches("^[0-9]{3}[0-9]{4}[0-9]{4}", tel);
        //when
        //then
        System.out.println(result);
    }
}
