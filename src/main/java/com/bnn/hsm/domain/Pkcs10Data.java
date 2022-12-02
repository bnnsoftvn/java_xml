package com.bnn.hsm.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class Pkcs10Data extends OtpChallengeRequest {

    private int dodaikhoa ;
    private String thuattoan;
}
