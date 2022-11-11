package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.Financial;
import com.capstone3.showbee.entity.FinancialDTO;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.MonthlyFinancial;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.FinancialRepository;
import com.capstone3.showbee.service.FinancialService;
import com.capstone3.showbee.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/financial")
@Tag(name="Financial", description = "가계부 관련")
public class FinancialController {
    private final FinancialRepository financialRepository;
    private final FinancialService financialService;
    private final ResponseService responseService;

    @Operation(summary = "가계부 전체 리스트로 가져오기", description = "헤더에 토큰 넣고 호출 시 해당 유저의 가계부 데이터 모두 가져옴")
    @GetMapping(value = "/lists")
    public ListResult<Financial> list(HttpServletRequest request) {
        return responseService.getListResult(financialService.findFAllByUser(request));
    }

    @Operation(summary = "가계부 생성",
            description = "헤더에 토큰 필요, 가계부 새로운 데이터 생성", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "FinancialDTO로 넘겨주세요"))
    @PostMapping(value = "/post")
    public Long postFin(@RequestHeader HttpServletRequest request, @RequestBody final FinancialDTO financialDTO) {
        return financialService.save(request, financialDTO).getFid();
    }

    @Operation(summary = "가계부 삭제",  description = "해당 fid의 가계부 데이터 삭제")
    @DeleteMapping(value = "/delete/{fid}")
    public CommonResult delete(@Parameter(name="fid", description = "삭제할 가계부의 fid(PK)") @PathVariable Long fid) {
        financialRepository.deleteById(fid);
        return responseService.getSuccessResult();
    }

    @PutMapping(value = "/modify")
    @Operation(summary = "가계부 수정", description = "헤더에 토큰 필요, 가계부 수정하기 - FinancialDTO의 fid 명시해주세요! 안 쓰면 새로 생성됨", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "FinancialDTO - fid 명시 필수"))
    public SingleResult<Financial> modifyFinancial(@RequestHeader HttpServletRequest request, @RequestBody final FinancialDTO financialDTO) {
        return responseService.getSingleResult(financialService.update(financialDTO, request));
    }

    @GetMapping("/get")
    @Operation(summary = "가계부 단일 조회", description = "가계부 fid를 통해 단일 건 조회하기")
    public Optional<Financial> getFinancialByFid(@Parameter(description = "가계부의 fid(PK)", name = "fid") @RequestParam Long fid) {
        return financialRepository.findById(fid);
    }

    @GetMapping("/getMonthly")
    @Operation(summary = "가계부 지출+수입 내역 월별", description = "헤더에 토큰 필요, 가계부 중 해당 월의 지출과 수입 모든 내역 - 날짜, 수입, 지출 / int[][0]: 수입, int[][1]: 지출")
    public Map<String, int[]> getMonthly(HttpServletRequest request, @Parameter(description = "현재 년-월(yyyy-MM) 형태로", name="nowDate") @RequestParam String nowDate) {
        return financialService.getInOutCome(request, nowDate);
    }

    @Operation(summary = "월 지출+수입(총)", description = "헤더에 토큰 필요, 가계부에서 해당 월의 지출, 수입 모두 계산 후 총합 return / int[][0]: 총수입, int[][1]: 총지출")
    @GetMapping("/getMonthlyTotal")
    public int[] getMonthlyTotal(HttpServletRequest request,@Parameter(description = "현재 년-월(yyyy-MM) 형태로", name="nowDate") @RequestParam String nowDate){
        return financialService.monthlyTotal(request, nowDate);
    }

    @Operation(summary = "가계부 전체 조회", description = "헤더에 토큰 필요, 해당 년-월의 가계부 전체 내역 return ")
    @GetMapping("/getlist")
    public Map<String, List<MonthlyFinancial>> getAll(HttpServletRequest request,@Parameter(description = "현재 년-월(yyyy-MM) 형태로", name="nowDate")  @RequestParam String nowDate){
        return financialService.getmonth(request, nowDate);
    }
}