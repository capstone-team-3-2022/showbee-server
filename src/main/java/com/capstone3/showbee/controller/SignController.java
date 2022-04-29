package com.capstone3.showbee.controller;


import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.exception.CEmailSigninFailedException;
import com.capstone3.showbee.exception.CUserExistException;
import com.capstone3.showbee.jwt.JwtTokenProvider;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.UserJpaRepository;
import com.capstone3.showbee.service.CustomUserDetailService;
import com.capstone3.showbee.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Api(tags = {"1. sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/v1")
public class SignController {

    private final UserJpaRepository userJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService userService;


    @ApiOperation(value = "로그인", notes = "이메일로 회원 로그인 ")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원  id: 이메일", required = true) @RequestParam String email,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userJpaRepository.findByEmail(email).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
    }
    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signin(@ApiParam(value = "회원 id: 이메일", required = true) @RequestParam String email,
                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                               @ApiParam(value = "이름(닉네임)", required = true) @RequestParam String name){


        if(userJpaRepository.findByEmail(email).isPresent()){
            throw new CUserExistException(); //존재하면 가입 X
        }
        userJpaRepository.save(User.builder()
        .email(email).password(passwordEncoder.encode(password)).name(name).roles(Collections.singletonList("ROLE_USER")).build());
        return responseService.getSuccessResult();
    }

    @GetMapping("check/{email}/exist") //버튼 눌러서 중복 확인 중복이면 true
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }
}