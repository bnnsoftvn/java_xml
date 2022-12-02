package com.bnn.hsm.domain;

import lombok.*;

@Data
@Builder
@Setter
@Getter
public class XmlDataWithRef {
    private String base64xml;
    private String hashalg;
    private String signdate;
    private String[] datapath;
    private String signaturepath;
    private String signatureid;
}
