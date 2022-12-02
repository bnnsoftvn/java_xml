package com.bnn.hsm.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Builder
public class ApiInfo
{
    private String appname;
    private String username;
    private String thuattoan;
    private int batbuocotp; /* 0 0 Khong tao */
    private int dodaikhoa; /* 0 Khong tao 2048 keylenth*/
    private int taocsr; /* 0 Tạo 1 không tạo*/
}