package com.bnn.hsm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.beans.ConstructorProperties;

@Data
@Setter
@Getter
@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Signature {
    private String value;
    private String hash;
    @ConstructorProperties({"value", "hash"})
    Signature(String value, String hash) {
        this.value = value;
        this.hash = hash;
    }
}

