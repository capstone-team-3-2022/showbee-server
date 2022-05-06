package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.exception.CUserNotFoundException;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.UserJpaRepository;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Secured("ROLE_USER")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserJpaRepository userJpaRepository;
    private final UserService userService;
    private final ResponseService responseService; //결과를 처리할 Service
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/users") //전체 회원 조회
    public ListResult<User> findAllUser(){
        return responseService.getListResult(userJpaRepository.findAll());
    }

    @GetMapping(value = "/user") //조회
    public SingleResult<User> findUser(HttpServletRequest request){
        User loginUser = userService.getUser(request);
        return responseService.getSingleResult(userJpaRepository.findByEmail(loginUser.getEmail()).orElseThrow(CUserNotFoundException::new));
    }

    @PutMapping(value = "/user")
    public SingleResult<User> modify(HttpServletRequest request, @RequestParam String name, @RequestParam String password){
        User loginUser = userService.getUser(request);
        User user = User.builder().id(loginUser.getId()).name(name).email(loginUser.getEmail()).password(passwordEncoder.encode(password)).roles(Collections.singletonList("ROLE_USER")).build();
        return responseService.getSingleResult(userJpaRepository.save(user));
    }

    @DeleteMapping(value = "/user/{id}")
    public CommonResult delete(@PathVariable Long id){
        userJpaRepository.deleteById(id);
        return responseService.getSuccessResult();
    }
}
