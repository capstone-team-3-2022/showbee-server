package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.ScheduleDTO;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.ScheduleRepository;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value="v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final ResponseService responseService;
    private final ScheduleService scheduleService;


    @PostMapping(value="/post")
    public Long postSch(HttpServletRequest request, @RequestBody final ScheduleDTO scheduleDTO) throws ParseException {
        return scheduleService.save(request, scheduleDTO).getSId();
    }


    @GetMapping(value="/get") //user의 일정 가져오기
    public List<Schedule> getlist(HttpServletRequest request){
        List<Schedule> result =  scheduleService.findAllByUser(request);
        return scheduleService.findAll(request);
    }


    @DeleteMapping(value = "/delete/{sid}")
    public CommonResult delete(@PathVariable Long sid){
        scheduleRepository.deleteById(sid);
        return responseService.getSuccessResult();
    }

}
