package com.bnn.hsm.util;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertUtils {
    private static CertificateFactory sCertFactory = null;
    private static void buildCertFactory() {
        if (sCertFactory != null) {
            return;
        }
        try {
            sCertFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new RuntimeException("Failed to create X.509 CertificateFactory", e);
        }
    }
        public static X509Certificate getCertificateFromBase64(byte [] encodedForm) throws CertificateException {
            if (sCertFactory == null) {
                buildCertFactory();
            }
            return generateCertificate(encodedForm, sCertFactory);
        }
    /**
     * Generates an {@code X509Certificate} from the encoded form using the provided
     * {@code CertificateFactory}.
     *
     * @throws CertificateException if the encodedForm cannot be decoded to a valid certificate.
     */
    public static X509Certificate generateCertificate(byte[] encodedForm,
                                                      CertificateFactory certFactory) throws CertificateException {
        X509Certificate certificate;
        try {
            certificate = (X509Certificate) certFactory.generateCertificate(
                new ByteArrayInputStream(encodedForm));
            return certificate;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

}
