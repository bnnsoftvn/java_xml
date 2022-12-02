package com.bnn.hsm.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AccountInfo
{
    private String username;
    private String sdt;
    private String email;
    private String madinhdanh;
    private String hovaten;
    private String madonvi;
}