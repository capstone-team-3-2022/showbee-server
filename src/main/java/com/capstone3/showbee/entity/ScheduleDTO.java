package com.capstone3.showbee.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDTO {
    private Long sId;

    private String stitle;
    private String content;
    private Integer price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Boolean shared;
    private Integer cycle;
    private String category;
    @Getter
    private List<String> participant;
    private boolean inoutcome;
    public Schedule toEntity(User user) throws ParseException {
        return Schedule.builder()
                .content(content).user(user).price(price).date(date).cycle(cycle).shared(shared).stitle(stitle).category(category)
                .sId(sId).inoutcome(inoutcome)
                .build();
    }

//
//    @Builder
//    public ScheduleDTO(String stitle, String category, String content, Integer price, Date date, Integer cycle, Boolean shared, List<String> participant) {
//        this.content = content;
//        this.price = price;
//        this.date = date;
//        this.cycle = cycle;
//        this.shared = shared;
//        this.stitle = stitle;
//        this.participant = participant;
//        this.category = category;
//    }

}
