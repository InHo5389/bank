package bank.learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LongTest {

    @Test
    @DisplayName("")
    void long_test_fail(){
        //given
        Long number1 = 1111L;
        Long number2 = 1111L;
        //when
        if (number1 == number2){
            System.out.println("동일");
        }else {
            System.out.println("동일x");
        }
        //then
    }

    @Test
    @DisplayName("")
    void long_test_success(){
        //given
        Long number1 = 1111L;
        Long number2 = 1111L;
        //when
        if (number1.longValue() == number2.longValue()){
            System.out.println("동일");
        }else {
            System.out.println("동일x");
        }
        //then
    }

    @Test
    @DisplayName("")
    void test(){
        //given
        Long v1 = 1000L;
        Long v2 = 1000L;
        //when
        //then
        Assertions.assertThat(v1).isEqualTo(v2);
    }
}
