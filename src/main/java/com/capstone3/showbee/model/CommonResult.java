package com.capstone3.showbee.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Tag(name = "CommonResult")
public class CommonResult {

    @Schema(description = "응답 성공 여부: true/false")
    private boolean success;

    @Schema(description = "응답 코드 번호: >0 정상, <0 비정상")
    private int code;

    @Schema(description = "응답 메시지 ")
    private String msg;
}
