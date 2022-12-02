package com.bnn.hsm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class RevocationStatus {
//    ERROR( 0, "Lỗi trong quá trình xác thực"),
//    EXPIRED( 1, "Chứng thư hết hạn"),
//    INVALID(2, "Chứng thư không hợp lệ"),
//    GOOD(3, "Chứng thư hợp lệ"),
//    UNKNOWN(4, "Lỗi không xác định"),
//    REVOKED(5, "Chứng thư bị thu hồi"),
//    UNCHECK(6,"Uncheck"),
//    UNTRUSTED(7,"Chứng thư không tin cậy");

    private int code;
    private String message;

    public RevocationStatus(int code, String message) {

        this.code = code;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}