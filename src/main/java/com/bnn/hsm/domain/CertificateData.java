package com.bnn.hsm.domain;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class CertificateData {
    private String base64Cert;
    private String date;
}

