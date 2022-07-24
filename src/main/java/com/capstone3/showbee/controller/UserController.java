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
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

//@Secured("ROLE_USER")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/getall") //전체 회원 조회
    public ListResult<User> findAllUser() {
        return userService.findAll();
    }

    @GetMapping(value = "/get") //조회
    public SingleResult<User> findUser(HttpServletRequest request){
        return userService.get(request);
    }

    @GetMapping(value = "/get/{email}")
    public SingleResult<User> findUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }


    @PutMapping(value = "/modify/name")
    public boolean modifyName(HttpServletRequest request, @RequestParam String name){
        return userService.modifyname(request, name);
    }

    @PutMapping(value = "/modify/pwd")
    public boolean modifyPwd(HttpServletRequest request, @RequestParam String password){
        return userService.modifyPwd(request, password);
    }


    @DeleteMapping(value = "/delete")
    public CommonResult delete(HttpServletRequest request){
        return userService.deleteUser(request);
    }


    @DeleteMapping(value = "/delete/{id}")
    public CommonResult deleteById(@PathVariable Long id){
        return userService.deleteById(id);
    }


}
