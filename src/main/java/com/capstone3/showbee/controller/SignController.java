package com.capstone3.showbee.controller;


import com.capstone3.showbee.entity.TokenDTO;
import com.capstone3.showbee.entity.TokenRequestDTO;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.exception.CEmailSigninFailedException;
import com.capstone3.showbee.exception.CUserExistException;
import com.capstone3.showbee.jwt.JwtTokenProvider;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.UserJpaRepository;
import com.capstone3.showbee.service.CustomUserDetailService;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.UserService;
<<<<<<< HEAD
=======
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
>>>>>>> 9a99719ab8053c0535b98774c41542f1f837b765
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/v1")
@Tag(name="Sign", description = "로그인 회원가입 관련")
public class SignController {

    private final UserService userService;
    private final ResponseService responseService;

    @Operation(summary = "로그인", description = "회원 로그인")
    @PostMapping(value = "/signin")
    public SingleResult<TokenDTO> signin(@Parameter(description = "가입할 때 사용한 email", name = "email") @RequestParam String email,
                                         @Parameter(description = "가입할 때의 password", name="password") @RequestParam String password) {
        return responseService.getSingleResult(userService.login(email, password));
    }

    @Operation(summary = "회원가입", description = "회원 생성")
    @PostMapping(value = "/signup")
    public CommonResult signin(@Parameter(name="email", description = "로그인 시 사용할 email") @RequestParam @NotBlank String email,
                               @Parameter(name="password", description = "로그인 시 사용할 password") @RequestParam @NotBlank String password,
                               @Parameter(name="name", description = "닉네임") @RequestParam @NotBlank String name){
        return userService.signup(email, password, name);
    }

    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 확인 -> 중복이면 true")
    @GetMapping(value = "check/{email}") //버튼 눌러서 중복 확인 중복이면 true
    public ResponseEntity<Boolean> checkEmailDuplicate(@Parameter(name="email", description = "중복 체크할 email") @PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @Operation(summary = "토큰 재발급", description = "accessToken, refreshToken 재발급 - refresh token 검증 해서 재발급", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "TokenRequestDTO로 넘겨주세요" ))
    @PostMapping("/reissue") //access, refresh token 재발급 - access token 만료 시 회원 검증 후 refresh token 검증해서 access, refresh token 재발급
    public SingleResult<TokenDTO> reissue(@RequestBody TokenRequestDTO tokenRequestDTO){
        return responseService.getSingleResult(userService.reissue(tokenRequestDTO));
    }
}