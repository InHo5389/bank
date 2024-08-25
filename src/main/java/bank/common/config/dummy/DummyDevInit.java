package bank.common.config.dummy;

import bank.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit {

    @Bean
    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    public CommandLineRunner init(UserRepository userRepository){
        return (args)->{
            // 서버 실행시에 무조건 실행된다.
            userRepository.save(new DummyObject().newUser("ssar","쌀"));
        };
    }
}
