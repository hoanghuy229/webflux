package com.hoanghuy2209.commonservice.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ValidateException extends RuntimeException{
    private final String  code;
    private final Map<String,String> messageMap;
    private final HttpStatus status;
    public ValidateException(String code,Map<String,String> message,HttpStatus status){
        this.code = code;
        this.messageMap = message;
        this.status = status;
    }
}