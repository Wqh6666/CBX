package com.campus.book.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class AddressDTO {
    private Long id;

    @NotBlank(message = "收货人不能为空")
    private String consignee;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    private String province = "上海市";
    private String city = "上海市";
    private String district;
    private String detail;
    private Integer isDefault = 0;
}
