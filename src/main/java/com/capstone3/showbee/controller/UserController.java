package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.exception.CUserNotFoundException;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.UserJpaRepository;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.UserService;
<<<<<<< HEAD
=======
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
>>>>>>> 9a99719ab8053c0535b98774c41542f1f837b765
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
@Tag(name="User", description = "유저 관련")
public class UserController {

    private final UserService userService;

    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보 가져오기")
    @GetMapping(value = "/getall") //전체 회원 조회
    public ListResult<User> findAllUser() {
        return userService.findAll();
    }

    @Operation(summary = "회원 단일 조회", description = "헤더의 토큰 기반으로 회원 정보 조회")
    @GetMapping(value = "/get") //조회
    public SingleResult<User> findUser(HttpServletRequest request){
        return userService.get(request);
    }

    @Operation(summary = "email로 유저 조회", description = "path에 넣은 이메일 기반으로 회원 정보 조회(단일)")
    @GetMapping(value = "/get/{email}")
    public SingleResult<User> findUserByEmail(@Parameter(name="email", description = "가입 시 작성한 email") @PathVariable String email){
        return userService.findUserByEmail(email);
    }

    @Operation(summary = "닉네임 수정", description = "헤더에 토큰 필요, name에 바꿀 이름 넣고 변경")
    @PutMapping(value = "/modify/name")
    public boolean modifyName(HttpServletRequest request,@Parameter(name = "name", description = "변경할 이름") @RequestParam String name){
        return userService.modifyname(request, name);
    }

    @Operation(summary = "비밀번호 변경", description = "헤더에 토큰 필요, pwd에 바꿀 비밀번호 넣고 변경")
    @PutMapping(value = "/modify/pwd")
    public boolean modifyPwd(HttpServletRequest request, @Parameter(name="password", description = "변경할 비밀번호") @RequestParam String password){
        return userService.modifyPwd(request, password);
    }

    @Operation(summary = "회원 삭제", description = "헤더에 넣은 토큰 기반으로 해당 유저 삭제(탈퇴)")
    @DeleteMapping(value = "/delete")
    public CommonResult delete(HttpServletRequest request){
        return userService.deleteUser(request);
    }

    @Operation(summary = "회원 삭제", description = "path에 삭제할 회원 id(PK)넣기")
    @DeleteMapping(value = "/delete/{id}")
    public CommonResult deleteById(@Parameter(name="id", description = "삭제할 유저의 id(PK)") @PathVariable Long id){
        return userService.deleteById(id);
    }


}
