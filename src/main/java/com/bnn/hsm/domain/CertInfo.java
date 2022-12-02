package com.bnn.hsm.domain;


import lombok.*;

import java.security.cert.X509Certificate;
import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@Setter
@Getter
public class CertInfo {
    public byte[] FileAnhCK;//Thông tin chữ kí
    private String APPID;
    private String Secret;
    private float TrangThai;
    private String MoTa;
    private String MaGD;
    private Date ExpiredDate;
    public X509Certificate CTSNguoidung; //Thông tin chứng thư của khách hàng trả về
    public X509Certificate CTSRootCA; //Thông tin chứng thư của root trả về
    public X509Certificate CTSSubCA; //Thông tin chứng thư của sub trả về
}
