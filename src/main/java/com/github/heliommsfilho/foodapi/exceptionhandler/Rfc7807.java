package com.github.heliommsfilho.foodapi.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Rfc7807 {

    private Integer status;
    private String type;
    private String title;
    private String detail;
}
