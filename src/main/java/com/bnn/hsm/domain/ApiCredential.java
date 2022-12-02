package com.bnn.hsm.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.time.Instant;


@Data
@Setter
@Getter
public class ApiCredential implements Serializable {

    private String apiKey;
    private String secret;
    private String username;
    private String appName;
    private int    mfaType;
    private String smartotp;
    private boolean active;
    private String base64Img;
    private String base64ImgUri;
    private String rawUrl;
    private Boolean createPermission;
    private Boolean status; /* 0 0 yeu cau otp 1 bat buoc otp */
    protected String nguoitao;
    protected Instant ngaytao;
    protected String nguoicapnhat;
    protected Instant ngaycapnhat;

    @Override
    public String toString() {
        return "ApiCredential{" +
                "apiKey='" + apiKey + '\'' +
                ", secret='" + secret + '\'' +
                ", username='" + username + '\'' +
                ", appName='" + appName + '\'' +
                "}";
    }
}
