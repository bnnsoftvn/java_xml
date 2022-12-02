package com.bnn.hsm.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.beans.ConstructorProperties;

@Data
@Setter
@Getter
@Value
public class CaCertResponse extends BaseResponse{
    private String code;
    @ConstructorProperties({"status", "message", "code"})
    CaCertResponse(int status, Object message, String code)
    {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}

