package com.bnn.hsm.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PdfOriginalPasswordData {
    private String base64pdf;
    private String hashalg;
    private int typesignature;
    private String signaturename;
    private String base64image;
    private String textout;
    private int pagesign;
    private int xpoint;
    private int ypoint;
    private int width;
    private int height;
    private String password;
    private String newPassword;
    private int fontsize;
    private String color;
}
