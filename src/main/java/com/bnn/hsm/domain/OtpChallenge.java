package com.bnn.hsm.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Getter
public class OtpChallenge {
    private String apikey;
    private String transactionid;
    private String challenge;
}
