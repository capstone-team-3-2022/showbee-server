package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.*;
import com.capstone3.showbee.model.MonthlySchedule;
import com.capstone3.showbee.repository.ScheduleRepository;
import com.capstone3.showbee.repository.SharedRepository;
import com.capstone3.showbee.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final SharedRepository sharedRepository;
    private final UserService userService;
    private final UserJpaRepository userJpaRepository;
    private final FinancialService financialService;

    //
    @Transactional
    public Schedule save(HttpServletRequest request, final ScheduleDTO scheduleDTO) throws ParseException {
        // userService의 getUser 호출
        User loginUser = userService.getUser(request);
        // scheduleDTO에 loginUser 값을 저장하고 sch에도 저장
        Schedule sch = scheduleRepository.save(scheduleDTO.toEntity(loginUser));
        // 만약 shared 값이 true라면
        if (scheduleDTO.toEntity(loginUser).getShared()) {
            // email값을 participant에 넣기
            for (String email : scheduleDTO.getParticipant()) {
                Shared sh = Shared.builder().user(userJpaRepository.findByEmail(email).get()).schedule(sch).build();
                // user랑 schedule값 저장
                sharedRepository.save(sh);
            }
        }
        // user, schedule에 title
        return sch;
    }

    public List<Schedule> findShared(HttpServletRequest request) {
        User loginUser = userService.getUser(request);
        List<Schedule> schl = scheduleRepository.findAllByUser(loginUser);
        List<Schedule> schedules = new ArrayList<>();
        for (Schedule s : schl) {
            if (s.getShared()) schedules.add(s);
        }
        return schedules;
    }

    public ScheduleDTO getById(Long sid) {
        Optional<Schedule> s = scheduleRepository.findById(sid);
        List<String> participant = new ArrayList<>();
        if (s.isPresent()) {
            Schedule schedule = s.get();
            List<Shared> sresult = sharedRepository.findAllBySchedule(schedule);
            for (Shared sh : sresult) {
                String uEmail = sh.getUser().getEmail();
                participant.add(uEmail);
            }
            return schedule.ScheduleToDTO(participant);
        } else {
            return null;
        }
    }


    public List<Schedule> findAll(HttpServletRequest request) {
        User loginUser = userService.getUser(request);
        List<Schedule> result = scheduleRepository.findAllByUser(loginUser);
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
        if (result.isPresent()) {
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
            int c = s.getCycle(); // 몇 달마다 고정으로 지출되는걸 달마다 가지고 와서. 계산한게 범위내(해당 달)에 들어가면 맵에 넣어준다.
            while (stringDate.compareTo(nowDate) < 0) {
                date = putCycle(date, c);
                stringDate = date.toString();
            }
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                while (stringDate.compareTo(nextDate) < 0) {
                    List<String> category = new ArrayList<>();
                    String day = stringDate.substring(8, 10);
                    if (monthlyMap.containsKey(day)) category = monthlyMap.get(day); //기존 map에 해당 날짜가 있을 때(중복 데이터)
                    //category에 또 다른 카테고리 추가하고 해당 날짜의 date에 다시 Put
                    category.add(s.getCategory());
                    monthlyMap.put(day, category);
                    date = putCycle(date, c);
                    stringDate = date.toString();
                }
            }
        }
        return monthlyMap;
    }

    public LocalDate putCycle(LocalDate date, int c) {
        switch (c) {
            case 7:
                date = date.plusDays(7);
                break;
            case 14:
                date = date.plusDays(14);
                break;
            case 1:
                date = date.plusMonths(1);
                break;
        }
        return date;
    }

    public int[] monthlyTotal(HttpServletRequest request, String nowDate) {
        String nextDate = financialService.getNextDate(nowDate);
        List<Schedule> result = findAll(request);
        int income = 0;
        int outcome = 0;
        for (Schedule s : result) {
            LocalDate date = s.getDate();
            String stringDate = date.toString();
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                if (s.getInoutcome()) income += s.getPrice();
                else outcome += s.getPrice();
            }
        }
        return new int[]{income, outcome};
    }

    public Map<String, List<MonthlySchedule>> getMonthList(HttpServletRequest request, String nowDate) {
        Map<String, List<MonthlySchedule>> gets = new TreeMap<>();
        User loginUser = userService.getUser(request);
        List<Schedule> alls = scheduleRepository.findAllByUser(loginUser);
        List<MonthlySchedule> msl;
        String nextDate = financialService.getNextDate(nowDate);
        for (Schedule s : alls) {
            LocalDate date = s.getDate();
            String stringDate = date.toString();
            int c = s.getCycle();
            while (stringDate.compareTo(nowDate) < 0) {
                date = putCycle(date, c);
                stringDate = date.toString();
            }
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                while (stringDate.compareTo(nextDate) < 0) {
                    String day = stringDate.substring(8, 10);
                    MonthlySchedule ms = MonthlySchedule.builder()
                            .price(s.getPrice()).sid(s.getSId()).stitle(s.getStitle()).build();
                    if (gets.containsKey(day)) msl = gets.get(day);
                     else msl = new ArrayList<>();

                    msl.add(ms);
                    gets.put(day, msl);
                    date = putCycle(date, c);
                    stringDate = date.toString();
                }
            }
        }

        return gets;
    }
}
