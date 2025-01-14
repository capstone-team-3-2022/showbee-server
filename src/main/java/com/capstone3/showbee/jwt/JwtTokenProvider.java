package com.capstone3.showbee.jwt;

import com.capstone3.showbee.entity.TokenDTO;
import com.capstone3.showbee.exception.CAuthenticationEntryPointException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    @Value("spring.jwt.secret")
    private String secretKey;
    private String ROLES = "roles";
    private final Long accessTokenValidTime = 60*60*1000L; //1 hour
    private final Long refreshTokenValidTime = 14*24*60*60*1000L; //2 weeks
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        //암호화
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes());

    }

    //jwt create
    public TokenDTO createToken(Long userPK, List<String> roles){
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPK));
        claims.put(ROLES, roles);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();


        return TokenDTO.builder().grantType("Bearer")
                .accessToken(accessToken).refreshToken(refreshToken)
                .accessTokenExpired(accessTokenValidTime).build();
    }

    //jwt로 인증 정보 조회
    public Authentication getAuthentication(String token){

        //jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        //권한 정보 x
        if(claims.get(ROLES) ==null){
            throw new CAuthenticationEntryPointException();
        }

//        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPK(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public String getUserPK(String token){
        return (Jwts.parser().setSigningKey(secretKey.getBytes())).parseClaimsJws(token).getBody().getSubject();
    }

    //jwt 토큰 복호화
    private Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
//        System.out.println(request.getHeader("X-AUTH-TOKEN"));
        return request.getHeader("X-AUTH-TOKEN");
    }
    //jwt 유효성 및 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }


}
