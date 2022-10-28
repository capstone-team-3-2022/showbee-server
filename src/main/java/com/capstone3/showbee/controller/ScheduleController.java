package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.ScheduleDTO;
import com.capstone3.showbee.entity.Shared;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.MonthlySchedule;
import com.capstone3.showbee.repository.ScheduleRepository;
import com.capstone3.showbee.service.ResponseService;
import com.capstone3.showbee.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="v1/schedule")
@RequiredArgsConstructor
@Tag(name="Schedule", description = "고정 지출 일정 관련")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final ResponseService responseService;
    private final ScheduleService scheduleService;


    // Schedule 추가하기
    @Operation(summary = "일정 생성", description = "새로 일정 생성, shared는 참가자가 있을 때 true로", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ScheduleDTO로 넘겨주기(id 기입 안해도 됨 - 자동 생성)"))
    @PostMapping(value="/post")
    public Long postSch(HttpServletRequest request, @RequestBody final ScheduleDTO scheduleDTO) throws ParseException {
        return scheduleService.save(request, scheduleDTO).getSId();
    }


    @Operation(summary = "사용자의 일정 모두 가져오기", description = "헤더의 토큰 기반으로 해당 유저의 모든 고정 지출 일정 가져오기(본인이 생성한 거, 공유받은 거 포함)")
    @GetMapping(value="/lists") //user의 일정 가져오기(모두, 공유받은거 포함)
    public List<Schedule> getlist(HttpServletRequest request){
        return scheduleService.findAll(request);
    }


    @Operation(summary = "일정 삭제", description = "sid(PK) 통해서 일정 삭제하기")
    @DeleteMapping(value = "/delete/{sid}")
    public CommonResult delete(@Parameter(name="sid", description ="삭제할 일정의 sid(PK)" ) @PathVariable Long sid){
        scheduleService.deleteSch(sid);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "일정 수정", description = "ScheduleDTO 통해서 일정 수정 - sid 명시해주세요, 헤더에 토큰 필수", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ScheduleDTO로 넘겨주세요"))
    @PutMapping("/modify")
    public Schedule update(@RequestBody final ScheduleDTO scheduleDTO, HttpServletRequest request) throws ParseException {
        return scheduleService.update(request, scheduleDTO);
    }

    @Operation(summary = "월간 고정 수입, 지출 총합계", description = "년-월에 해당하는 모든 고정 일정 월별 총합으로 return, 헤더에 토큰 필수, int[0]: 수입, int[1]: 지출")
    @GetMapping("/getMonthlyTotal")
    public int[] getMonthlyTotal(HttpServletRequest request,@Parameter(name="nowDate", description = "해당 년-월(yyyy-MM)") @RequestParam String nowDate){
        return scheduleService.monthlyTotal(request, nowDate);
    }

    @Operation(summary = "월간 고정 일정 - 카테고리", description = "년-월에 해당하는 모든 고정 일정의 카테고리를 return(일별로) - 메인화면 아이콘 표시 용, 같은 날에 고정 일정 여러 개면 카테고리도 여러 개 반환")
    @GetMapping("/getMonthly")
    public Map<String, List<String>> getMonthlyCategory(HttpServletRequest request,@Parameter(name = "nowDate", description = "해당 년-월(yyyy-MM)") @RequestParam String nowDate){
        return scheduleService.getCategoryMonthly(request, nowDate);
    }

    @Operation(summary = "공유 일정 조회", description = "로그인한 유저의 공유된 일정만 조회, 헤더에 토큰 필요 - 참가자는 반환x")
    @GetMapping("/getShared") //공유 일정만
    public List<Schedule> findShared(HttpServletRequest request){
        return scheduleService.findShared(request);
    }

    @Operation(summary = "일정 단일 조회", description = "sid에 해당하는 일정 정보 조회(단일)")
    @GetMapping("/get")
    public ScheduleDTO get(@Parameter(name="sid", description = "일정의 sid(PK)") @RequestParam Long sid){
        return  scheduleService.getById(sid);
    }

    @Operation(summary = "월간 고정 일정 조회 (모두)", description = "헤더에 토큰 필수, 로그인한 유저의 해당 년-월의 고정 일정 조회(모두), 해당 달의 sid, 제목, 금액 반환(메인화면 - 리스트 뷰로 변경 시 사용)")
    @GetMapping("/getlist")
    public Map<String, List<MonthlySchedule>> getlist(HttpServletRequest request, @Parameter(name="nowDate", description = "해당 년-월(yyyy-MM)") @RequestParam String nowDate){
        return scheduleService.getMonthList(request, nowDate);
    }

    @Operation(summary = "공유받은 일정만 조회", description = "헤더에 토큰 필수, 로그인한 유저의 공유받은 일정 조회(참가자도 포함해서 반환)")
    @GetMapping("/getShareList") //공유 받은 일정만
    public List<ScheduleDTO> ShareList(HttpServletRequest request){
        return scheduleService.ShareList(request);
    }

}
