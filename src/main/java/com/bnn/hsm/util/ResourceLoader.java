package com.bnn.hsm.util;

import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;

public class ResourceLoader {

    public static String DEFAULT_BASE64_IMAGE;
    public static String DEFAULT_BASE64_CER;
    public static byte[] DEFAULT_FONT;
    public static byte[] DEFAULT_PDF;
    public static byte[] DEFAULT_PDF_PASSWORD;

    static {
        try {
            byte[] pngbytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("HCVN.png"));
            DEFAULT_BASE64_IMAGE = Base64Utils.base64Encode(pngbytes);
            byte[] cer = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("certocheck.cer"));
            DEFAULT_BASE64_CER = Base64Utils.base64Encode(cer);
            DEFAULT_FONT = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("segoeui.ttf"));
            DEFAULT_PDF = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf"));
            DEFAULT_PDF_PASSWORD = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("test_password.pdf"));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
