package com.rtravez.msa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BaseResponseDto<T> {
    @Builder.Default
    private Integer code = 200;
    private String message;
    private List<String> errors;
    private T data;
}
