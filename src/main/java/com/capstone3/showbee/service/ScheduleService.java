package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.*;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

//@RequiredArgsConstructor
@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final SharedRepository sharedRepository;
    private final UserService userService;
    private final UserJpaRepository userJpaRepository;
    private final FinancialService financialService;

    @Transactional
    public Schedule save(HttpServletRequest request, final ScheduleDTO scheduleDTO) throws ParseException {
        User loginUser = userService.getUser(request);
        Schedule sch = scheduleRepository.save(scheduleDTO.toEntity(loginUser));
        if (scheduleDTO.toEntity(loginUser).getShared()) {
            for (String email : scheduleDTO.getParticipant()) {
                Shared sh = Shared.builder().user(userJpaRepository.findByEmail(email).get()).schedule(sch).build();
                sharedRepository.save(sh);
            }
        }
        return sch;
    }


    public List<Schedule> findAllByUser(HttpServletRequest request) {
        User loginUser = userService.getUser(request);
        return scheduleRepository.findAllByUser(loginUser);
    }

    public List<Schedule> findAll(HttpServletRequest request) {
        List<Schedule> result = findAllByUser(request);
        User loginUser = userService.getUser(request);
        List<Shared> sresult = sharedRepository.findAllByUser(loginUser);
        for (Shared sh : sresult) {
            Schedule schedule = sh.getSchedule();
            result.add(schedule);
        }
        return result;
    }

    public void deleteSch(Long sId) {
        List<Shared> sharedSch = sharedRepository.findAllBySchedule(scheduleRepository.getById(sId));
        for (Shared sh : sharedSch) {
            Shared.builder().schedule(null).user(null).id(sh.getId()).build();
            //update
            sharedRepository.deleteById(sh.getId());
        }
        scheduleRepository.deleteById(sId);
    }

    public Schedule update(HttpServletRequest request, ScheduleDTO scheduleDTO) throws ParseException {
        Long sid = scheduleDTO.getSId();
        Optional<Schedule> result = scheduleRepository.findById(sid);
        User loginUser = userService.getUser(request);
        if(result.isPresent()){
            List<Shared> sharedSch = sharedRepository.findAllBySchedule(scheduleRepository.getById(sid));
            if (!sharedSch.isEmpty()) {
                for (Shared s : sharedSch) {
                    Shared.builder().schedule(null).user(null).id(s.getId()).build();
                    //update
                    sharedRepository.deleteById(s.getId());
                }
            }
        }
        return save(request, scheduleDTO);
    }

    //inoutcome아니고 category
    public Map<String, List<String>> getCategoryMonthly(HttpServletRequest request, String nowDate) {
        Map<String, List<String>> monthlyMap = new TreeMap<>();
        User loginUser = userService.getUser(request);
        List<Schedule> result = scheduleRepository.findAllByUser(loginUser);
        String nextDate = financialService.getNextDate(nowDate);
        for (Schedule s : result) {
            LocalDate date = s.getDate();
            String stringDate = date.toString(); //가계부에 있는 데이터들의 날짜
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                List<String> category = new ArrayList<>();
                String day = stringDate.substring(8,10);
                if (monthlyMap.containsKey(day)) { //기존 map에 해당 날짜가 있을 때(중복 데이터)
                    category = monthlyMap.get(day);
                    category.add(s.getCategory());
                    //category에 또 다른 카테고리 추가하고 해당 날짜의 date에 다시 Put
                }
                else {
                    category.add(s.getCategory());
                }

                monthlyMap.put(day, category);
            }
        }
        return monthlyMap;
    }

    public int[] monthlyTotal(HttpServletRequest request, String nowDate) {
        String nextDate = financialService.getNextDate(nowDate);
        List<Schedule> result = findAllByUser(request);
        int income = 0;
        int outcome = 0;
        for(Schedule s: result){
            LocalDate date = s.getDate();
            String stringDate = date.toString();
            if(stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0){
                if (s.getInoutcome()) income += s.getPrice();
                else outcome += s.getPrice();
            }
        }
        return new int[]{income, outcome};
    }

}
