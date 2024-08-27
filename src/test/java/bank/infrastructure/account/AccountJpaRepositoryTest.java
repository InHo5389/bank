package bank.infrastructure.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountJpaRepositoryTest {

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    @DisplayName("")
    void test(){
        //given
        //when
        //then
        System.out.println(accountJpaRepository.findByUserId(1L));
    }

}