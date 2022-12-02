package com.bnn.hsm.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Builder
public class ApiStatus {
    private String apikey;
    private int status;
}
