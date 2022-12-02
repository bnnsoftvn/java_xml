package com.bnn.hsm.domain;

import lombok.*;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
public class CreateCACert {
    protected String Passport;// số chứng minh/hộ chiếu/căn cước
    protected int passport_type;//loại 1 Giấy CMND   //loại 2 Hộ Chiếu  //loại 3 Căn cước công dân
    protected String passport_date;// Ngày cấp định dạng YYYY-MM-dd
    protected String passport_place;// Nơi cấp
    protected String Fullname ; //Họ và tên
    protected String phone;//số điện thoại
    protected String address;
    protected String email;
    protected int gender; //1: Nam, 2 : Nữ, 3: khac
    protected String birthday; //Ngày sinh định dạng dữ liệu  (YYYY-MM-DD or YYYY)
    protected String file_passport_base64;
    protected String file_passport_name;
}

