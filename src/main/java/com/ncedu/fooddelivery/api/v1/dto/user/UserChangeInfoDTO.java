package com.ncedu.fooddelivery.api.v1.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeInfoDTO {

    @Size(min=6, max=50)
    private String fullName;

    //client additional fields
    @Size(min=11, max=20)
    private String phoneNumber;
}
