package com.bnn.hsm.domain;

import lombok.*;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
public class UpdateCACert  {
    protected String Passport;
    protected int passport_type;
    protected String passport_date;
    protected String passport_place;
    protected String Fullname ;
    protected String phone;
    protected String address;
    protected String email;
    protected int gender;
    protected String birthday;
    protected String file_passport_base64;
    protected String file_passport_name;
    private String tranid;

}
