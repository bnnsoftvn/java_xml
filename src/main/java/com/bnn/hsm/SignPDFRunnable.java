package com.bnn.hsm;

import com.bnn.hsm.domain.ApiResp;
import com.bnn.hsm.domain.PdfOriginalData;
import com.bnn.hsm.domain.RespCode;
import com.bnn.hsm.hmac.ApiClient;
import com.bnn.hsm.util.Base64Utils;
import com.bnn.hsm.util.CbUtils;
import com.bnn.hsm.util.ResourceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SignPDFRunnable implements Runnable {

    private static final String BASE_URL = "http://localhost:8086";
    private static final String APP_ID = "602e4999cf4d4fb3ae4999cf4d5fb39e";
    private static final String SECRET = "NTk1ZjE2MzkyOWFkZDRhZTAyYTI0ZTNlYzRkODA4NGI2OWI4MTI0MDk3OGZlM2JmYzIxMjY1OTZjNTk2YjI5ZQ==";

    int i;

    SignPDFRunnable(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        int k = 10;
        while(k-->0) {
            ApiClient client = null;
            try {
                client = new ApiClient(BASE_URL);
                client.setCredentials(APP_ID, SECRET);

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

                byte[] bytes = Files.readAllBytes(Paths.get("C:\\tmp\\TAL_" + i + ".pdf"));

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
                        .base64pdf(Base64Utils.base64Encode(bytes))
                        .build();
                HttpResponse result = client.Post("/api/v2/pdf/sign/originaldata", pdfOriginalData);
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResp rs = objectMapper.readValue(result.getEntity().getContent(), ApiResp.class);
                if (rs.getStatus() == RespCode.OK.value()) {
                    System.out.println("Success" + i);
                    byte[] file_signed = Base64Utils.base64Decode(rs.getObj().toString());
                    FileOutputStream fos = new FileOutputStream("C:\\tmp\\TAL_signed_" + i + ".pdf");
                    fos.write(file_signed);
                    fos.close();
                } else {
                    System.out.println("Kí không thành công. Chi tiết:" + rs.getError() + rs.getDescription());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}