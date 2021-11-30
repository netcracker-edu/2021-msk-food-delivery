package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class UserChangeInfoDTO {

    @Size(min=6, max=50)
    private String fullName;

    //client additional fields
    @Size(min=11, max=20)
    private String phoneNumber;
}
