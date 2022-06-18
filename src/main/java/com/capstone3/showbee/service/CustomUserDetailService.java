package com.capstone3.showbee.service;

import com.capstone3.showbee.exception.CUserNotFoundException;
import com.capstone3.showbee.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserJpaRepository userJpaRepo;

    @Override
    public UserDetails loadUserByUsername(String userPK){
        return userJpaRepo.findById(Long.valueOf(userPK)).orElseThrow(CUserNotFoundException::new);
    }

//    public boolean checkEmailDuplicate(String email){
//        return userJpaRepo.existsByEmail(email);
//    }
}
