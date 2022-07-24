package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.*;
import com.capstone3.showbee.exception.CEmailSigninFailedException;
import com.capstone3.showbee.exception.CRefreshTokenException;
import com.capstone3.showbee.exception.CUserExistException;
import com.capstone3.showbee.exception.CUserNotFoundException;
import com.capstone3.showbee.jwt.JwtTokenProvider;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.RefreshTokenRepository;
import com.capstone3.showbee.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService {

    private final UserJpaRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ResponseService responseService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public User getUser(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public TokenDTO login(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        //accessToken, RefreshToken 발급
        TokenDTO tokenDTO = jwtTokenProvider.createToken(user.getId(), user.getRoles());

        //RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                                    .key(user.getId()).token(tokenDTO.getRefreshToken()).build();
        refreshTokenRepository.save(refreshToken);
        return tokenDTO;
    }

    public CommonResult signup(String email, String password, String name){
        if(userRepository.findByEmail(email).isPresent()){
            throw new CUserExistException(); //존재하면 가입 X
        }
        userRepository.save(User.builder()
                .email(email).password(passwordEncoder.encode(password)).name(name).roles(Collections.singletonList("ROLE_USER")).build());
        return responseService.getSuccessResult();
    }

    public TokenDTO reissue(TokenRequestDTO tokenRequestDTO){ //access, refresh token 재발급
        //만료된 refresh token 에러
        if (!jwtTokenProvider.validateToken(tokenRequestDTO.getRefreshToken())){
            throw new CRefreshTokenException();
        }

        //accessToken에서 username(pk) 가져오기
        String accessToken = tokenRequestDTO.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //user pk로 user 검색 - repos에 저장된 refresh token이 없음?
        User user = userRepository.findById(Long.parseLong(authentication.getName())).orElseThrow(CUserNotFoundException::new);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(user.getId()).orElseThrow(CRefreshTokenException::new);

        //access, refresh token 재발급 + refresh token save
        TokenDTO newCreatedToken = jwtTokenProvider.createToken(user.getId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);
        return newCreatedToken;
    }

    public boolean checkEmailDuplicate(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean modifyname(HttpServletRequest request, @RequestParam String name){
        User loginUser = getUser(request);
        if(name==null||name.equals("")) return false;
        else{
            User user = User.builder().id(loginUser.getId()).name(name).email(loginUser.getEmail()).password(loginUser.getPassword())
                    .roles(Collections.singletonList("ROLE_USER")).build();
            userRepository.save(user);
            return true;
        }
    }

    public boolean modifyPwd(HttpServletRequest request, @RequestParam String password){
        User loginUser = getUser(request);
        if(password==null||password.equals("")) return false;
        else{
            User user = User.builder()
                    .id(loginUser.getId()).name(loginUser.getName()).email(loginUser.getEmail()).password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER")).build();
            userRepository.save(user);
            return true;
        }
    }

    public CommonResult deleteUser(HttpServletRequest request){
        User user = getUser(request);
        userRepository.deleteById(user.getId());
        return responseService.getSuccessResult();
    }

    public CommonResult deleteById(Long id){
        userRepository.deleteById(id);
        return responseService.getSuccessResult();
    }

    public SingleResult<User> findUserByEmail(String email){
        return responseService.getSingleResult(userRepository.findByEmail(email)
                                                .orElseThrow(CUserNotFoundException::new));
    }

    public SingleResult<User> get(HttpServletRequest request){
        User loginUser = getUser(request);
        return responseService.getSingleResult(userRepository.findById(loginUser.getId()).orElseThrow(CUserNotFoundException::new));
    }

    public ListResult<User> findAll(){
        return responseService.getListResult(userRepository.findAll());
    }
}
