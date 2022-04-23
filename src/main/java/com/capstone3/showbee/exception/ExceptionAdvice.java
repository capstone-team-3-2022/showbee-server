package com.capstone3.showbee.exception;

import com.capstone3.showbee.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult defaultException(HttpServletRequest request, Exception e){
//        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
//    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String defaultException(HttpServletRequest request, Exception e){
        return "알 수 없는 오류가 발생했습니다.";
    }

//    @ExceptionHandler(CEmailSigninFailedException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e){
//        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
//    }

    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e){
        return "계정이 존재하지 않거나 이메일 또는 비밀번호가 정확하지 않습니다.";
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e){
        return "해당 리소스에 접근하기 위한 권한이 없습니다.";
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String userNotFound(HttpServletRequest request, CUserNotFoundException e){
        return "존재하지 않는 회원입니다.";
    }

    @ExceptionHandler(AccessDeniedException.class)
//    public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException e){
//        return responseService.getFailResult(Integer.valueOf())
//    }
    public String AccessDeniedException(HttpServletRequest request, AccessDeniedException e){
        return "보유한 권한으로 접근할 수 없는 리소스입니다.";
    }


    //code 정보에 해당하는 메시지를 조회
    private String getMessage(String code){
        return getMessage(code);
    }

    //code 정보, 추가 argument로 현재 locale에 맞는 메시지 조회
//    private String getMessage(String code, Object[] args){
//        return messageSource.getMessage(code, args);
//    }

}

