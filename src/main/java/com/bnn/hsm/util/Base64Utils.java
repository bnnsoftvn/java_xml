package com.bnn.hsm.util;
import java.util.Base64;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Base64Utils {
    public static String base64Encode(byte[] aData) {
        byte[] encode = Base64.getEncoder().encode(aData);
        return new String(encode);
    }

    /**
     * Decodes the given Base64-encoded data, as specified in RFC-2045 (Section
     * 6.8).
     *
     * @param aData the Base64-encoded aData.
     * @return the decoded <var>aData</var>.
     * @exception IllegalArgumentException if NULL or empty data is passed
     */
    public static byte[] base64Decode(String aData) {
        return Base64.getDecoder().decode(aData);
    }

    public static byte[] getImageBytes(String base64Source)
    {
        // tokenize the data
        String[] parts = base64Source.split(",");
        String imageString = parts[1];
        return base64Decode(imageString);
    }

}
