package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.Financial;
import com.capstone3.showbee.entity.FinancialDTO;
import com.capstone3.showbee.entity.Shared;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.repository.FinancialRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@AllArgsConstructor
public class FinancialService {

    private final FinancialRepository financialRepository;
    private final UserService userService;

    public List<Financial> findFAllByUser(HttpServletRequest request){
        User loginUser = userService.getUser(request);
        return financialRepository.findFAllByUser(loginUser);

    }
    @Transactional
    public Long save(HttpServletRequest request, final FinancialDTO financialDTO){
        User loginUser = userService.getUser(request);
        return financialRepository.save(financialDTO.toEntity(loginUser)).getFid();
    }

}
