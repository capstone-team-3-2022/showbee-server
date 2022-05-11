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
import java.util.Optional;

//@Secured("ROLE_USER")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserJpaRepository userJpaRepository;
    private final UserService userService;
    private final ResponseService responseService; //결과를 처리할 Service
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/getall") //전체 회원 조회
    public ListResult<User> findAllUser(){
        return responseService.getListResult(userJpaRepository.findAll());
    }

    @GetMapping(value = "/get") //조회
    public SingleResult<User> findUser(HttpServletRequest request){
        User loginUser = userService.getUser(request);
        return responseService.getSingleResult(userJpaRepository.findByEmail(loginUser.getEmail()).orElseThrow(CUserNotFoundException::new));
    }

    @GetMapping(value = "/get/{email}")
    public SingleResult<User> findUserByEmail(@PathVariable String email){
        return responseService.getSingleResult(userJpaRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new));
    }

    @PutMapping(value = "/modify/name")
    public SingleResult<User> modifyName(HttpServletRequest request, @RequestParam String name){
        User loginUser = userService.getUser(request);
        System.out.println("password: "+loginUser.getPassword());
        User user = User.builder()
                        .id(loginUser.getId()).name(name).email(loginUser.getEmail()).password(loginUser.getPassword())
                        .roles(Collections.singletonList("ROLE_USER")).build();
        return responseService.getSingleResult(userJpaRepository.save(user));
    }

    @PutMapping(value = "/modify/pwd")
    public SingleResult<User> modifyPwd(HttpServletRequest request, @RequestParam String password){
        User loginUser = userService.getUser(request);
        User user = User.builder()
                        .id(loginUser.getId()).name(loginUser.getName()).email(loginUser.getEmail()).password(passwordEncoder.encode(password))
                        .roles(Collections.singletonList("ROLE_USER")).build();
        return responseService.getSingleResult(userJpaRepository.save(user));
    }


    @DeleteMapping(value = "/delete")
    public CommonResult delete(HttpServletRequest request){
        User user = userService.getUser(request);
        userJpaRepository.deleteById(user.getId());
        return responseService.getSuccessResult();
    }


    @DeleteMapping(value = "/delete/{id}")
    public CommonResult deleteById(@PathVariable Long id){
        userJpaRepository.deleteById(id);
        return responseService.getSuccessResult();
    }
}
