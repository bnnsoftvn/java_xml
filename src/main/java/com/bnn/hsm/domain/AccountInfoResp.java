package com.bnn.hsm.domain;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class AccountInfoResp {
    private String username;
    private String sdt;
    private String email;
    private String madinhdanh;
    private String hovaten;
    private String madonvi;
}
