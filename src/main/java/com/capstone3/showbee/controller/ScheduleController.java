package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.ScheduleDTO;
import com.capstone3.showbee.entity.Shared;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.MonthlySchedule;
import com.capstone3.showbee.repository.ScheduleRepository;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final ResponseService responseService;
    private final ScheduleService scheduleService;


    // Schedule 추가하기
    @PostMapping(value="/post")
    public Long postSch(HttpServletRequest request, @RequestBody final ScheduleDTO scheduleDTO) throws ParseException {
        return scheduleService.save(request, scheduleDTO).getSId();
    }


    @GetMapping(value="/lists") //user의 일정 가져오기(모두, 공유받은거 포함)
    public List<Schedule> getlist(HttpServletRequest request){
        return scheduleService.findAll(request);
    }


    @DeleteMapping(value = "/delete/{sid}")
    public CommonResult delete(@PathVariable Long sid){
        scheduleService.deleteSch(sid);
        return responseService.getSuccessResult();
    }


    @PutMapping("/modify")
    public Schedule update(@RequestBody final ScheduleDTO scheduleDTO, HttpServletRequest request) throws ParseException {
        return scheduleService.update(request, scheduleDTO);
    }

    @GetMapping("/getMonthlyTotal")
    public int[] getMonthlyTotal(HttpServletRequest request, String nowDate){
        return scheduleService.monthlyTotal(request, nowDate);
    }

    @GetMapping("/getMonthly")
    public Map<String, List<String>> getMonthlyCategory(HttpServletRequest request, String nowDate){
        return scheduleService.getCategoryMonthly(request, nowDate);
    }

    @GetMapping("/getShared") //공유 일정만
    public List<Schedule> findShared(HttpServletRequest request){
        return scheduleService.findShared(request);
    }

    @GetMapping("/get")
    public ScheduleDTO get(Long sid){
        return  scheduleService.getById(sid);
    }

    @GetMapping("/getlist")
    public Map<String, List<MonthlySchedule>> getlist(HttpServletRequest request, String nowDate){
        return scheduleService.getMonthList(request, nowDate);
    }

    @GetMapping("/getShareList") //공유 받은 일정만
    public List<Schedule> ShareList(HttpServletRequest request){
        return scheduleService.ShareList(request);
    }

}
