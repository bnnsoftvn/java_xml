package com.bnn.hsm;


import com.bnn.hsm.domain.OtpChallenge;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ActiveMFA {
    private String apiKey;
    private int mfaType;
    OtpChallenge otpChallenge;
    public static final int MFA_DEACTIVE = 0;
    public static final int MFA_ACTIVE = 1;
}

