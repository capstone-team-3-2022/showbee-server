package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.ScheduleDTO;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.repository.ScheduleRepository;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final ResponseService responseService;
    private final ScheduleService scheduleService;

    @GetMapping("/lists")
    public ListResult<Schedule> list(){
        return responseService.getListResult(scheduleRepository.findAll());
    }

    @PostMapping("/post")
    public Long postSch(HttpServletRequest request, @RequestBody final ScheduleDTO scheduleDTO){
        return scheduleService.save(request, scheduleDTO);
    }



}
