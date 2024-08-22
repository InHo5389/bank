package bank.common.config.jwt;

import bank.common.config.auth.LoginUser;
import bank.domain.user.User;
import bank.domain.user.UserEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtProcessTest {
    
    @Test
    @DisplayName("")        
    void jwt_create(){
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        //when
        String token = JwtProcess.create(loginUser);
        //then
        assertThat(token.startsWith(JwtUtil.TOKEN_PREFIX));
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("")
    void jwt_verify(){
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        String token = JwtProcess.create(loginUser);
        token = token.replace(JwtUtil.TOKEN_PREFIX,"");
        //when
        LoginUser verifyLoginUser = JwtProcess.verify(token);
        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(verifyLoginUser.getUser().getId());
        assertThat(loginUser.getUser().getRole()).isEqualTo(verifyLoginUser.getUser().getRole());
    }

}