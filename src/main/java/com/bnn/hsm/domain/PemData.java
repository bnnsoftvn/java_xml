package com.bnn.hsm.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PemData  extends OtpChallengeRequest{
    private String endcert;
    private String subcert;
    private String rootcert;
}
