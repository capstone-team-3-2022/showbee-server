package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.ScheduleDTO;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.jwt.JwtTokenProvider;
import com.capstone3.showbee.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

//@RequiredArgsConstructor
@Service
@AllArgsConstructor
public class ScheduleService {
    private final JwtTokenProvider jwtTokenProvider;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long save(HttpServletRequest request, final ScheduleDTO scheduleDTO){
        String token = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        User loginUser = (User) authentication.getPrincipal();

        return scheduleRepository.save(scheduleDTO.toEntity(loginUser)).getSId();
    }


}
