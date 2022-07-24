package com.capstone3.showbee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDTO {
    private Long sId;
    @Column(nullable = false)
    private String stitle;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Integer price;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Boolean shared;
    @Column(nullable = false)
    private Integer cycle;
    @Column(nullable = false)
    private String category;
//    @Getter
    private List<String> participant; //참가자 가져오기??

    @Column(nullable = false)
    private boolean inoutcome;

    public Schedule toEntity(User user){
        return Schedule.builder()
                .content(content).user(user).price(price).date(date).cycle(cycle).shared(shared).stitle(stitle).category(category)
                .sId(sId).inoutcome(inoutcome).participant(participant)
                .build();
    }

}
