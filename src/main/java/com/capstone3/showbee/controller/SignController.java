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
import io.swagger.annotations.Api;
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
public class SignController {

    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping(value = "/signin")
    public SingleResult<TokenDTO> signin(@RequestParam String email, @RequestParam String password) {
        return responseService.getSingleResult(userService.login(email, password));
    }

    @PostMapping(value = "/signup")
    public CommonResult signin(@RequestParam @NotBlank String email, @RequestParam @NotBlank String password, @RequestParam @NotBlank String name){
        return userService.signup(email, password, name);
    }

    @GetMapping(value = "check/{email}") //버튼 눌러서 중복 확인 중복이면 true
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @PostMapping("/reissue") //access, refresh token 재발급 - access token 만료 시 회원 검증 후 refresh token 검증해서 access, refresh token 재발급
    public SingleResult<TokenDTO> reissue(@RequestBody TokenRequestDTO tokenRequestDTO){
        return responseService.getSingleResult(userService.reissue(tokenRequestDTO));
    }
}