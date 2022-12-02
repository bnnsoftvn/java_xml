package com.bnn.hsm.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PdfOriginalData {
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

    public String getBase64pdf() {
        return base64pdf;
    }
    public void setBase64pdf(String base64pdf) {
        this.base64pdf = base64pdf;
    }

    public String getHashalg() {
        return hashalg;
    }
    public void setHashalg(String hashalg) {
        this.hashalg = hashalg;
    }

    public int getTypesignature() {
        return typesignature;
    }
    public void setTypesignature(int typesignature) {
        this.typesignature = typesignature;
    }

    public String getSignaturename() {
        return signaturename;
    }
    public void setSignaturename(String signaturename) {
        this.signaturename = signaturename;
    }

    public String getBase64image() {
        return base64image;
    }
    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public String getTextout() {
        return textout;
    }
    public void setTextout(String textout) {
        this.textout = textout;
    }

    public int getPagesign() {
        return pagesign;
    }
    public void setPagesign(int pagesign) {
        this.pagesign = pagesign;
    }

    public int getXpoint() {
        return xpoint;
    }
    public void setXpoint(int xpoint) {
        this.xpoint = xpoint;
    }

    public int getYpoint() {
        return ypoint;
    }
    public void setYpoint(int ypoint) {
        this.ypoint = ypoint;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
}
