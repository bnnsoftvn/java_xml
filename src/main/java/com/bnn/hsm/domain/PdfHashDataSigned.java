package com.bnn.hsm.domain;

public class PdfHashDataSigned {
    private String base64signature;
    private int status;
    private String description;

    public String getBase64signature() {
        return base64signature;
    }
    public void setBase64signature(String base64signature) {
        this.base64signature = base64signature;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
