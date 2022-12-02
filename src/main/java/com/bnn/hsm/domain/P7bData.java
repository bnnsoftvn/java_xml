package com.bnn.hsm.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class P7bData extends OtpChallengeRequest {
    private String p7bbase64;
}