package com.bnn.hsm.hmac;

import com.bnn.hsm.util.Base64Utils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.*;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.tls.HashAlgorithm;


import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class HmacSignatureBuilder {

    private static final String HMAC_SHA_512 = "HmacSHA512";
    private static final byte DELIMITER = '\n';
    private String algorithm = HMAC_SHA_512;
    private String scheme;
    private String host;
    private String method;
    private String resource;
    private String nonce;
    private String apiKey;
    private byte[] apiSecret;
    private byte[] payload;
    private String date;
    private String contentType;

    public String getAlgorithm() {
        return algorithm;
    }

    public HmacSignatureBuilder algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public HmacSignatureBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public HmacSignatureBuilder host(String host) {
        this.host = host;
        return this;
    }

    public HmacSignatureBuilder apiKey(String key) {
        this.apiKey = key;
        return this;
    }

    public HmacSignatureBuilder method(String method) {
        this.method = method;
        return this;
    }

    public HmacSignatureBuilder resource(String resource) {
        this.resource = resource;
        return this;
    }

    public HmacSignatureBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HmacSignatureBuilder date(String dateString) {
        this.date = dateString;
        return this;
    }


    public HmacSignatureBuilder nonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public HmacSignatureBuilder apiSecret(byte[] secretBytes) {
        this.apiSecret = secretBytes;
        return this;
    }

    public HmacSignatureBuilder apiSecret(String secretString) {
        this.apiSecret = secretString.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public HmacSignatureBuilder payload(byte[] payloadBytes) {
        this.payload = payloadBytes;
        return this;
    }
    public static final Digest createHash(String hashAlgorithm)
    {
        switch (hashAlgorithm)
        {
            case "HmacSHA256":
                return new SHA256Digest();
            case "HmacSHA512":
                return new SHA512Digest();
            default:
                throw new IllegalArgumentException("unknown HashAlgorithm");
        }
    }

    public byte[] build() {

        final HMac digest = new HMac(createHash(algorithm));
        digest.init(new KeyParameter(apiSecret));
        byte[] bytes = method.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes,0,bytes.length);
        digest.update(DELIMITER);
//        bytes = scheme.getBytes(StandardCharsets.UTF_8);
//        digest.update(bytes,0,bytes.length);
//        digest.update(DELIMITER);
//        bytes = host.getBytes(StandardCharsets.UTF_8);
//        digest.update(bytes,0,bytes.length);
//        digest.update(DELIMITER);

        bytes = resource.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes,0,bytes.length);
        digest.update(DELIMITER);
        if (contentType != null) {
            bytes = contentType.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes,0,bytes.length);
            digest.update(DELIMITER);
        }
        bytes =  apiKey.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes,0,bytes.length);
        digest.update(DELIMITER);
        if (nonce != null) {
            bytes = nonce.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes,0,bytes.length);
            digest.update(DELIMITER);
        }
        bytes = date.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes,0,bytes.length);
        digest.update(DELIMITER);

        digest.update(payload,0,payload.length);
        digest.update(DELIMITER);


        byte[] buf2 = new byte[digest.getMacSize()];
        digest.doFinal(buf2,0);
        digest.reset();
        return buf2;
    }

    public boolean isHashEquals(byte[] expectedSignature) {
        final byte[] signature = build();
        return MessageDigest.isEqual(signature, expectedSignature);
    }

    public String buildAsBase64String() {
        final byte[] signature = build();
        return Base64Utils.base64Encode(signature);
    }
}
