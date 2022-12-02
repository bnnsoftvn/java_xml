package com.bnn.hsm.domain;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApiCredentialResp{
    private String csr;
    private ApiCredential credential;
}
