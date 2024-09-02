package bank.controller.user;

import bank.controller.common.response.CustomApiResponse;
import bank.controller.user.dto.UserRequest;
import bank.domain.user.UserService;
import bank.domain.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public CustomApiResponse<UserResponse.Join> join(@Valid @RequestBody UserRequest.Join request){
        return CustomApiResponse.ok(userService.join(request.toCommand()));
    }
}
