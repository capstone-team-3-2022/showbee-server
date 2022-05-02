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
import java.util.List;

//@RequiredArgsConstructor
@Service
@AllArgsConstructor
public class ScheduleService {
    private final JwtTokenProvider jwtTokenProvider;
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    @Transactional
    public Long save(HttpServletRequest request, final ScheduleDTO scheduleDTO){
        User loginUser = userService.getUser(request);

        return scheduleRepository.save(scheduleDTO.toEntity(loginUser)).getSId();
    }


    public List<Schedule> findAllByUser(HttpServletRequest request){
        User loginUser = userService.getUser(request);
        return scheduleRepository.findAllByUser(loginUser);
    }



}
