package com.bnn.hsm.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Getter
public class PdfHashData {
    private String base64hash;
    private String hashalg;
    private OtpChallenge otpChallenge;
}
