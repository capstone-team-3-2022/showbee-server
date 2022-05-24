package com.capstone3.showbee.service;

import com.capstone3.showbee.entity.Financial;
import com.capstone3.showbee.entity.FinancialDTO;
import com.capstone3.showbee.entity.Shared;
import com.capstone3.showbee.entity.User;
import com.capstone3.showbee.model.MonthlyFinancial;
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
        Optional<Financial> result = financialRepository.findById(fid);
        if (result.isPresent()) {
            User loginUser = userService.getUser(request);
            return financialRepository.save(financialDTO.toEntity(loginUser));
        } else return null;
    }

    public Map<String, List<MonthlyFinancial>> getmonth(HttpServletRequest request, String nowDate){
        Map<String, List<MonthlyFinancial>> getf = new TreeMap<>();
        User loginUser = userService.getUser(request);
        List<Financial> allf = financialRepository.findFAllByUser(loginUser);
        List<MonthlyFinancial> mfl;
        String nextDate = getNextDate(nowDate);
        for(Financial f: allf){
            Date date = f.getDate();
            String stringDate = date.toString();
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                String day = stringDate.substring(8,10);
                MonthlyFinancial mf =  MonthlyFinancial.builder()
                        .price(f.getPrice()).inoutcome(f.getInoutcome()).content(f.getContent()).category(f.getCategory()).fid(f.getFid()).build();
                if (getf.containsKey(day)){
                    mfl= getf.get(day);
                    mfl.add(mf);

                } else {
                    mfl = new ArrayList<>();
                    mfl.add(mf);
                }
                getf.put(day, mfl);
            }
        }

        return getf;
    }
    public Map<String, int[]> getInOutCome(HttpServletRequest request, String nowDate) {
        Map<String, int[]> dateMap = new TreeMap<>();
        User loginUser = userService.getUser(request);
        List<Financial> result = financialRepository.findFAllByUser(loginUser);
        String nextDate = getNextDate(nowDate);

        for (Financial f : result) {
            Date date = f.getDate();
            String stringDate = date.toString(); //가계부에 있는 데이터들의 날짜
            if (stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0) {
                int[] money;
                String day = stringDate.substring(8,10);
                System.out.println(day);
                if (dateMap.containsKey(day)) money = new int[]{dateMap.get(day)[0], dateMap.get(day)[1]};
                else money = new int[]{0, 0};

                if (f.getInoutcome()) money[0] += f.getPrice();
                else money[1] += f.getPrice();
                dateMap.put(day, money);
            }
        }
        return dateMap;
    }

    public int[] monthlyTotal(HttpServletRequest request, String nowDate) {
        String nextDate = getNextDate(nowDate);
        User loginUser = userService.getUser(request);
        List<Financial> result = financialRepository.findFAllByUser(loginUser);
        int income = 0;
        int outcome = 0;
        for(Financial f: result){
            Date date = f.getDate();
            String stringDate = date.toString();
            if(stringDate.compareTo(nowDate) >= 0 && stringDate.compareTo(nextDate) < 0){
                if (f.getInoutcome()) income += f.getPrice();
                else outcome += f.getPrice();
            }
        }
        return new int[]{income, outcome};
    }

    public String getNextDate(String nowDate) {
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
        return nextDate;
    }



}
