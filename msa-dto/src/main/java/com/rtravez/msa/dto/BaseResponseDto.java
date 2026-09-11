package com.rtravez.msa.dto;

import java.util.Map;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResponseDto<T> {

    @Builder.Default
    private int status = 200;
    @Nullable
    private String detail;
    @Nullable
    private Map<String, Object> properties;
    private T data;

}
