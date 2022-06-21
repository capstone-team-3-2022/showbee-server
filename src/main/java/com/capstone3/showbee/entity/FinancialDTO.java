package com.capstone3.showbee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialDTO {
    private Long fid;
    @Column(nullable = false)
    private String content;
    private String category;
    @Column(nullable = false)
    private int price;
    private String bank;
    private String memo;
    @Column(nullable = false)
    private boolean inoutcome;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(nullable = false)
    private LocalDate date;

    public Financial toEntity(User user){
        return Financial.builder()
                .category(category).user(user).price(price).date(date).content(content).fid(fid)
                .bank(bank).memo(memo).inoutcome(inoutcome)
                .build();
    }


}
