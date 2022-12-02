package com.bnn.hsm.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class OtpChallengeRequest {
    protected OtpChallenge otpChallenge;
}
