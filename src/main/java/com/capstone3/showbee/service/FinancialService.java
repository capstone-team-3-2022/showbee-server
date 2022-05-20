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
import java.util.*;

@Service
@AllArgsConstructor
public class FinancialService {

    private final FinancialRepository financialRepository;
    private final UserService userService;

    public List<Financial> findFAllByUser(HttpServletRequest request) {
        User loginUser = userService.getUser(request);
        return financialRepository.findFAllByUser(loginUser);

    }

    @Transactional
    public Financial save(HttpServletRequest request, final FinancialDTO financialDTO) {
        User loginUser = userService.getUser(request);
        return financialRepository.save(financialDTO.toEntity(loginUser));
    }

    public Financial update(FinancialDTO financialDTO, HttpServletRequest request) {
        Long fid = financialDTO.getFid();
        System.out.println("FID: " + fid);
        Optional<Financial> result = financialRepository.findById(fid);
        if (result.isPresent()) {
            User loginUser = userService.getUser(request);
            return financialRepository.save(financialDTO.toEntity(loginUser));
        } else return null;
    }

    public Map<Date, int[]> getInOutCome(HttpServletRequest request, String nowDate) {
        User loginUser = userService.getUser(request);
        List<Financial> result = financialRepository.findFAllByUser(loginUser);
        Map<Date, int[]> dateMap = new HashMap<>();
        String year = nowDate.substring(0, 5);
        int month = Integer.parseInt(nowDate.substring(5));
        month++;
        String nextDate = "";
        switch (month) {
            case 13:
                month = 1;
                int nextyear = Integer.parseInt(year.substring(0, 4)) + 1;
                year = Integer.toString(nextyear) + "-";
            default:
                nextDate = year + "0" + Integer.toString(month);
                break;
            case 12:
            case 11:
                nextDate = year + Integer.toString(month);
                break;
        }
        for (Financial f : result) {
            Date date = f.getDate();
            String stringDate = date.toString();
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                int[] money;
                if (dateMap.containsKey(date)) money = new int[]{dateMap.get(date)[0], dateMap.get(date)[1]};
                else money = new int[]{0, 0};

                if (f.getInoutcome()) money[0] += f.getPrice();
                else money[1] += f.getPrice();

                dateMap.put(date, money);
            }
        }
        dateMap.forEach((k, v) -> {
            System.out.println(k + ":" + v[0] + "," + v[1]);
        });
        return dateMap;
    }

}
