package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.ScheduleDTO;
import com.capstone3.showbee.entity.Shared;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.jwt.JwtTokenProvider;
import com.capstone3.showbee.repository.ScheduleRepository;
import com.capstone3.showbee.repository.SharedRepository;
import com.capstone3.showbee.repository.UserJpaRepository;
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
    private final SharedRepository sharedRepository;
    private final UserService userService;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public Long save(HttpServletRequest request, final ScheduleDTO scheduleDTO){
        User loginUser = userService.getUser(request);
        Schedule sch = scheduleRepository.save(scheduleDTO.toEntity(loginUser));
        if(scheduleDTO.toEntity(loginUser).getShared()){
            for (String email: scheduleDTO.getParticipant()){
                Shared sh = Shared.builder().user(userJpaRepository.findByEmail(email).get()).schedule(sch).build();
                sharedRepository.save(sh);
            }
        }
        return sch.getSId();
    }


    public List<Schedule> findAllByUser(HttpServletRequest request){
        User loginUser = userService.getUser(request);
        return scheduleRepository.findAllByUser(loginUser);
    }



}
