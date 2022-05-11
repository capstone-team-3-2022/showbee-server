package com.capstone3.showbee.controller;

import com.capstone3.showbee.entity.Financial;
import com.capstone3.showbee.entity.FinancialDTO;
import com.capstone3.showbee.model.CommonResult;
import com.capstone3.showbee.model.ListResult;
import com.capstone3.showbee.model.SingleResult;
import com.capstone3.showbee.repository.FinancialRepository;
import com.capstone3.showbee.service.FinancialService;
import com.capstone3.showbee.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/financial")
public class FinancialController {

    private final FinancialRepository financialRepository;
    private final FinancialService financialService;
    private final ResponseService responseService;


    @GetMapping(value = "/lists")
    public ListResult<Financial> list(HttpServletRequest request) {
        return responseService.getListResult(financialService.findFAllByUser(request));
    }

    @PostMapping(value = "/post")
    public Long postFin(HttpServletRequest request, @RequestBody final FinancialDTO financialDTO) {
        return financialService.save(request, financialDTO);
    }

//    @PutMapping(value="/modify")
//    public SingleResult<Financial> modifyFinancial(HttpServletRequest request, @RequestBody final FinancialDTO financialDTO, long fid){
//        financialService.update(financialDTO, fid);
//    }


    @DeleteMapping(value = "/delete/{fid}")
    public CommonResult delete(@PathVariable Long fid) {
        financialRepository.deleteById(fid);
        return responseService.getSuccessResult();
    }


}