package com.bnn.hsm.domain;

import lombok.*;

import java.beans.ConstructorProperties;

@NoArgsConstructor
@Data
@Setter
@Getter
public class BaseResponse {
    public static final int SUCCSESS_STATUS = 1;
    public static final int ERROR_STATUS = 2;
    protected Object message;
    protected int status;
    @ConstructorProperties({"status", "message"})
    BaseResponse(int status, Object message)
    {
        this.status = status;
        this.message = message;
    }

}
