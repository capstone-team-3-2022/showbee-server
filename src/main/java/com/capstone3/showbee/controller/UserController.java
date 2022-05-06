package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.exception.CUserNotFoundException;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.UserJpaRepository;
import com.capstone3.showbee.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Secured("ROLE_USER")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserJpaRepository userJpaRepository;
    private final ResponseService responseService; //결과를 처리할 Service
    private final PasswordEncoder passwordEncoder;
    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @GetMapping(value = "/users")
    public ListResult<User> findAllUser(){
        return responseService.getListResult(userJpaRepository.findAll());
    }

    @GetMapping(value = "/user")
    public SingleResult<User> findUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(email);
        return responseService.getSingleResult(userJpaRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new));
    }

    @PutMapping(value = "/user")
    public SingleResult<User> modify(@RequestParam String name, @RequestParam String password){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((User)authentication.getPrincipal()).getId();
        String email = authentication.getName();
        User user = User.builder().id(id).name(name).email(email).password(passwordEncoder.encode(password)).roles(Collections.singletonList("ROLE_USER")).build();
        return responseService.getSingleResult(userJpaRepository.save(user));
    }

    @DeleteMapping(value = "/user/{id}")
    public CommonResult delete(@PathVariable Long id){
        userJpaRepository.deleteById(id);
        //성공 결과 정보만 필요한 경우 getSuccessResult()를 이요하여 결과 출력
        return responseService.getSuccessResult();
    }
}
