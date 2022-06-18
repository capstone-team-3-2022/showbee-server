package com.capstone3.showbee.controller;


import com.capstone3.showbee.entity.User;
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

import java.util.Collections;

@Api(tags = {"1. sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/v1")
public class SignController {

    private final UserService userService;

    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }


    @PostMapping(value = "/signup")
    public CommonResult signin(@RequestParam String email, @RequestParam String password, @RequestParam String name){
        return userService.signup(email, password, name);
    }

    @GetMapping(value = "check/{email}") //버튼 눌러서 중복 확인 중복이면 true
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }
}