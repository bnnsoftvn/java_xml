package com.bnn.hsm;


import com.bnn.hsm.domain.*;
import com.bnn.hsm.hmac.ApiClient;
import com.bnn.hsm.util.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Test {

    private static final String BASE_URL = "https://sign-hn1.vin-hsm.com";
    private static final String USR_APP_ID = "4288161d272f4622b23e52697346ad8d";
    private static final String USR_SECRET = "eHi3x+c4nQZYuVQRZV3d1WcB46aCzMQhNR4KcSpY";

    static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, IOException, CodeGenerationException {

        Test.SignXMLRefs();

    }

    private static void SignXMLRefs() {
        String xmlBase64 = "";
        ApiClient client = null;
        try {
            byte[] bytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("test2.xml"));
            xmlBase64 = Base64Utils.base64Encode(bytes);
            client = new ApiClient(BASE_URL);
            client.setCredentials(USR_APP_ID, USR_SECRET);
            java.text.DateFormat format = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String signdate = format.format(new Date());
            XmlDataWithRef xmlDataWithRef =
            XmlDataWithRef.builder()
                    .base64xml(xmlBase64)
                    .signatureid("LVCDT1")
                    .signdate(signdate)
                    .datapath(new String[]{"LenhVanChuyen/DuLieu/ThongTinChung", "LenhVanChuyen/DuLieu/NoiDungLenhVanChuyen", "LenhVanChuyen/DuLieu/NoiDungBenDiXacNhan" })
                    .signaturepath("LenhVanChuyen/DanhSachChuKySo/BenDi")
                    .hashalg("SHA-1")
                    .build();

            HttpResponse result = client.Post("/api/v2/xml/refs", xmlDataWithRef);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("SignXML:" + rs.getObj());
                FileOutputStream fos = new FileOutputStream("InvoiceSigned.xml");
                byte[] signeddata = Base64Utils.base64Decode(rs.getObj().toString());
                fos.write(signeddata);
                fos.close();
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void SignPdfPassword() {

        ApiClient client = null;
        try {
            client = new ApiClient(BASE_URL);
            client.setCredentials(USR_APP_ID, USR_SECRET);

            String sHash = "SHA256";
            int typeSignature = CbUtils.TYPE_PDFSIGNATURE_TEXT;
            String base64Image = ResourceLoader.DEFAULT_BASE64_IMAGE;
            String textOut = "Cyberlotus Vietnam";
            int pageSign = 1;
            String signatureName = "Signature";
            int xPoint = 100;
            int yPoint = 200;
            int width = 100;
            int height = 100;


            PdfOriginalPasswordData pdfOriginalData = PdfOriginalPasswordData.builder().base64image(base64Image)
                    .hashalg(sHash)
                    .textout(textOut)
                    .pagesign(pageSign)
                    .signaturename(signatureName)
                    .xpoint(xPoint)
                    .ypoint(yPoint)
                    .width(width)
                    .height(height)
                    .typesignature(typeSignature)
                    .base64pdf(Base64Utils.base64Encode(ResourceLoader.DEFAULT_PDF_PASSWORD))
                    .password("123123")
                    .newPassword("456456")
                    .fontsize(14)
                    .color("#161616")
                    .build();
            HttpResponse result = client.Post("/api/v2/pdf/sign/password", pdfOriginalData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("Success");
                byte[] file_signed = Base64Utils.base64Decode(rs.getObj().toString());
                FileOutputStream fos = new FileOutputStream("pdfsigned.pdf");
                fos.write(file_signed);
                fos.close();
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
    }

    private static void VerifyCert() {
        ApiClient client = null;
        try {
            client = new ApiClient(BASE_URL);
            client.setCredentials(USR_APP_ID, USR_SECRET);


            // Lấy chứng thư số của user a8ff044d4cff44e5bf044d4cff34e526
            String cer = ResourceLoader.DEFAULT_BASE64_CER;
            java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ssZZ");
            CertificateData certificateData = CertificateData.builder().base64Cert(cer).date(format.format(new Date())).build();

            HttpResponse result = client.Post("/api/certificate/verify", certificateData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                int code = (int) ((LinkedHashMap) rs.getObj()).get("code");
                String message = (String) ((LinkedHashMap) rs.getObj()).get("message");
                System.out.println(message);
            } else {
                log.error("Lỗi chi tiết:" + rs.getError() + rs.getDescription());
            }

        } catch (Exception ex) {

        }


    }

    public static class BinaryData extends OtpChallengeRequest {
        private String alg;
        private String payload;

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }
    }
    public static void  SignXML() {
        String xmlBase64 = "";
        ApiClient client = null;
        try {
            byte[] bytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xml"));
            xmlBase64 = Base64Utils.base64Encode(bytes);
            client = new ApiClient(BASE_URL);
            client.setCredentials(USR_APP_ID, USR_SECRET);
            XmlOriginalData xmlOriginalData = new XmlOriginalData();
            xmlOriginalData.setBase64xml(xmlBase64);
            xmlOriginalData.setHashalg("SHA-256");
            HttpResponse result = client.Post("/api/v2/xml/sign/defaultdata", xmlOriginalData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("SignXML:" + rs.getObj());
                FileOutputStream fos = new FileOutputStream("InvoiceSigned.xml");
                byte[] signeddata = Base64Utils.base64Decode(rs.getObj().toString());
                fos.write(signeddata);
                fos.close();
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void SignPdf() throws NoSuchAlgorithmException {

        ApiClient client = null;
        try {
            client = new ApiClient(BASE_URL);
            client.setCredentials(USR_APP_ID, USR_SECRET);

            String sHash = "SHA256";
            int typeSignature = CbUtils.TYPE_PDFSIGNATURE_TEXT;
            String base64Image = ResourceLoader.DEFAULT_BASE64_IMAGE;
            String textOut = "Cyberlotus Vietnam";
            int pageSign = 1;
            String signatureName = "Signature";
            int xPoint = 100;
            int yPoint = 200;
            int width = 100;
            int height = 100;


            PdfOriginalData pdfOriginalData = PdfOriginalData.builder().base64image(base64Image)
                    .hashalg(sHash)
                    .textout(textOut)
                    .pagesign(pageSign)
                    .signaturename(signatureName)
                    .xpoint(xPoint)
                    .ypoint(yPoint)
                    .width(width)
                    .height(height)
                    .typesignature(typeSignature)
                    .base64pdf(Base64Utils.base64Encode(ResourceLoader.DEFAULT_PDF))
                    .build();
            HttpResponse result = client.Post("/api/v2/pdf/sign/originaldata", pdfOriginalData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("Success");
                byte[] file_signed = Base64Utils.base64Decode(rs.getObj().toString());
                FileOutputStream fos = new FileOutputStream("pdfsigned.pdf");
                fos.write(file_signed);
                fos.close();
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
    }

    public static void SignHashPdf() throws NoSuchAlgorithmException {

        ApiClient client = null;
        try {
            client = new ApiClient(BASE_URL);

            //Thông tin Authenticate
            client.setCredentials(USR_APP_ID, USR_SECRET);
            HttpResponse get = client.Get("/api/account/endcert");


            // Lấy chứng thư số của user a8ff044d4cff44e5bf044d4cff34e526
            String responseAsString = EntityUtils.toString(get.getEntity());
            byte[] base64Decode = Base64Utils.base64Decode(responseAsString);

            ByteArrayInputStream certBytes = new ByteArrayInputStream(base64Decode);
            java.security.cert.CertificateFactory cf
                    = java.security.cert.CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate certificate = cf.generateCertificate(certBytes);
            java.security.cert.Certificate chain[] = {certificate};

            // Đọc file pdf test trong resource
            byte[] pdfbytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf"));


            PdfUtils pdf = new PdfUtils();

            // Thuật toán sử dụng
            String sHash = "SHA-256";
            // Loại chữ kí
            //            public static int TYPE_PDFSIGNATURE_DONTSHOW = 0;
            //            public static int TYPE_PDFSIGNATURE_TEXT = 1;
            //            public static int TYPE_PDFSIGNATURE_IMA = 2;
            //            public static int TYPE_PDFSIGNATURE_TEXTIMA = 3;
            int typeSignature = CbUtils.TYPE_PDFSIGNATURE_TEXTIMA;

            // Ảnh dạng base64 binary
            String base64Image = ResourceLoader.DEFAULT_BASE64_IMAGE;

            // Nội dung hiển thị chữ kí

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String noidung = "Kí bởi: BAOVIET Vietnam\n";
            noidung += "Kí ngày: " + formatter.format(new Date());

            // Font ch?, M?u s?c k?ch th??c c?a n?i dung
            BaseColor colorSign = new BaseColor(0, 128, 0);
            byte[] bytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("segoeui.ttf"));

            final BaseFont bf = BaseFont.createFont("segoeui.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes, null);
            Font font = new Font(bf, 9, Font.NORMAL, colorSign);

            //Trang kí
            int pageSign = 1;
            // Id chữ kí 1 file pdf
            String signatureName = UUID.randomUUID().toString();
            int xPoint = 100;
            int yPoint = 200;
            int width = 100;
            int height = 100;

            // Hash file pdf
            byte[] hashPdfFileV2 = pdf.hashPdfFileV2(pdfbytes, chain, sHash, typeSignature, pageSign, signatureName, base64Image, noidung, xPoint, yPoint, width, height, font);

            PdfHashData pdfHashData = new PdfHashData();
            pdfHashData.setBase64hash(Base64Utils.base64Encode(hashPdfFileV2));
            pdfHashData.setHashalg(sHash);

            HttpResponse result = client.Post("/api/v2/pdf/sign/hashdata", pdfHashData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("Success");
                System.out.println("PDf:");
                byte[] sigBytes = Base64Utils.base64Decode(rs.getObj().toString());
                byte[] pdfout = pdf.addExternalSignature(sigBytes);
                FileOutputStream fos = new FileOutputStream("pdfsigned.pdf");
                fos.write(pdfout);
                fos.close();
                result = client.Post("/api/v2/pdf/verify", Base64Utils.base64Encode(pdfout));
                rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
                log.info(rs.toString());
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
    }

    public static void SignDocx(ApiCredentialResp acc) throws NoSuchAlgorithmException {
        ApiClient client = null;
        try {
            client = new ApiClient(BASE_URL);
            client.setCredentials(acc.getCredential().getApiKey(), acc.getCredential().getSecret());
            HttpResponse get = client.Get("/api/account/endcert");
            InputStream docx = Thread.currentThread().getContextClassLoader().getResourceAsStream("test2.docx");
            byte[] docxBytes = IOUtils.toByteArray(docx);

            OfficeOriginalData officeOriginalData = new OfficeOriginalData();
            officeOriginalData.setHashalg("SHA-1");
            officeOriginalData.setBase64office(Base64Utils.base64Encode(docxBytes));

            HttpResponse result = client.Post("/api/v2/office/sign/originaldata", officeOriginalData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("signed docx:" + rs.getObj());
                FileOutputStream fos = new FileOutputStream("test_signed.docx");
                byte[] signeddata = Base64Utils.base64Decode(rs.getObj().toString());
                fos.write(signeddata);
                fos.close();
                result = client.Post("/api/v2/office/verify", Base64Utils.base64Encode(signeddata));
                rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
                log.info(rs.toString());
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + ", detail descscription: " + rs.getDescription());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SignInvoice(ApiCredentialResp acc) {
        String xmlBase64 = "";
        ApiClient client = null;
        try {
            byte[] bytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xml"));
            xmlBase64 = Base64Utils.base64Encode(bytes);
            client = new ApiClient(BASE_URL);
            client.setCredentials(acc.getCredential().getApiKey(), acc.getCredential().getSecret());
            XmlInvoiceData xmlInvoiceData = new XmlInvoiceData();
            xmlInvoiceData.setBase64xml(xmlBase64);
            xmlInvoiceData.setHashalg("SHA1");

            HttpResponse result = client.Post("/api/v2/xml/sign/invoicedatav1", xmlInvoiceData);
            ObjectMapper objectMapper = createObjectMapper();
            ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
            if (rs.getStatus() == RespCode.OK.value()) {
                System.out.println("InvoiceSigned:" + rs.getObj());
                FileOutputStream fos = new FileOutputStream("InvoiceSigned.xml");
                byte[] signeddata = Base64Utils.base64Decode(rs.getObj().toString());
                fos.write(signeddata);
                fos.close();
            } else {
                log.error("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

