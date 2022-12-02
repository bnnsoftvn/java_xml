package com.bnn.hsm.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

public class PdfUtils {
    private static PdfReader reader = null;
    private static ByteArrayOutputStream count = null;
    private static PdfStamper stp = null;
    private static PdfSignatureAppearance sap;
    private static OcspClient ocspClient = null;
    private static TSAClient tsaClient = null;
    private static Collection<byte[]> crlBytes = null;
    private static MakeSignature.CryptoStandard sigtype = MakeSignature.CryptoStandard.CMS;
    private static int signatureNumber = 0;


    public byte[] hashPdfFileV2(byte[] pdfContent, Certificate[] chain, String sHash,
                                int typeSignature, int pageSign, String signatureName,
                                String base64Image, String noidung,
                                int xPoint, int yPoint,
                                int width, int height, Font font2) throws IOException, DocumentException, GeneralSecurityException {

        int estimatedSize = 8192;
        if (crlBytes != null) {
            for (byte[] element : crlBytes) {
                estimatedSize += element.length + 10;
            }
        }
        if (ocspClient != null)
            estimatedSize += 4192;
        if (tsaClient != null)
            estimatedSize += 4192;

        reader = new PdfReader(pdfContent);
        count = new ByteArrayOutputStream();
        AcroFields af = reader.getAcroFields();
        ArrayList signatureNameNames = af.getSignatureNames();
        signatureNumber = signatureNameNames.size() + 1;
        if (signatureNumber > 1) {
            stp = PdfStamper.createSignature(reader, count, '\0', null, true);
        } else {
            stp = PdfStamper.createSignature(reader, count, '\0');
        }
        sap = stp.getSignatureAppearance();

        Rectangle rSignature = new Rectangle(xPoint, yPoint, xPoint+ width, yPoint + height);
        sap.setVisibleSignature(rSignature, pageSign, signatureName);



        sap.setCertificate(chain[0]);
        if (sigtype == MakeSignature.CryptoStandard.CADES) {
            sap.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL2);
        }

        if (typeSignature == CbUtils.TYPE_PDFSIGNATURE_TEXT) {

            sap.setLayer2Font(font2);
            sap.setLayer2Text(noidung);
        }

        if (typeSignature == CbUtils.TYPE_PDFSIGNATURE_IMA) {
            byte[] bytePng = Base64Utils.base64Decode(base64Image);
            Image image = Image.getInstance(bytePng);
            sap.setImage(image);
            sap.setAcro6Layers(true);
            sap.setLayer2Text("");
        }

        if (typeSignature == CbUtils.TYPE_PDFSIGNATURE_TEXTIMA) {
            sap.setLayer2Font(font2);


            byte[] bytePng = Base64Utils.base64Decode(base64Image);
            Image image = Image.getInstance(bytePng);
            image.scalePercent(50);
            image.setAbsolutePosition(100f, 150f);
            image.setAlignment(0);
            sap.setImage(image);
            sap.setImageScale(0.3f);
            sap.setAcro6Layers(true);
            sap.setLayer2Text(noidung);
        }

        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, sigtype == MakeSignature.CryptoStandard.CADES ? PdfName.ETSI_CADES_DETACHED : PdfName.ADBE_PKCS7_DETACHED);
        dic.setReason(sap.getReason());
        dic.setLocation(sap.getLocation());
        dic.setSignatureCreator(sap.getSignatureCreator());
        dic.setContact(sap.getContact());
        dic.setDate(new PdfDate(sap.getSignDate()));
        sap.setCryptoDictionary(dic);

        InputStream data;
        HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
        Integer value = new Integer(estimatedSize * 2 + 2);
        exc.put(PdfName.CONTENTS, value);
        sap.preClose(exc);
        data = sap.getRangeStream();
        ExternalDigest externalDigest = new BouncyCastleDigest();
        return DigestAlgorithms.digest(data, externalDigest.getMessageDigest(sHash));
    }


    public static byte[] addExternalSignature(byte[] extSignature) throws IOException, DocumentException {

        int estimatedSize = 8192;
        if (estimatedSize + 2 < extSignature.length) {
            throw new IOException("Not enough space");
        }

        byte[] paddedSig = new byte[estimatedSize];
        System.arraycopy(extSignature, 0, paddedSig, 0, extSignature.length);

        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);
        count.flush();
        count.close();
        reader.close();
        return count.toByteArray();
    }

    public byte[] signPdf(byte[] hashPdfFileV2, PrivateKey privateKey, Certificate[] chain, String hashAlgorithm, String prov_name) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, IOException, DocumentException {
        Calendar cal = Calendar.getInstance();
        PdfPKCS7 sgn1 = new PdfPKCS7(privateKey, chain, hashAlgorithm, prov_name, null, false);
        byte[] sh = sgn1.getAuthenticatedAttributeBytes(hashPdfFileV2, null, null,  MakeSignature.CryptoStandard.CMS);
        sgn1.update(sh, 0, sh.length);
        byte[] encodedPKCS7 = sgn1.getEncodedPKCS7(hashPdfFileV2, null, null, null, MakeSignature.CryptoStandard.CMS);
        byte[] pdf_byte = addExternalSignature(encodedPKCS7);
        return pdf_byte;
    }
}
