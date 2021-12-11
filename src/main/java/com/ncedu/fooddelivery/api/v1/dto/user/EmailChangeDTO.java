package com.ncedu.fooddelivery.api.v1.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EmailChangeDTO {

    @NotBlank
    @Email(regexp = EMAIL_REGEXP)
    private String email;
    @NotBlank
    @Size(min=8, max=64)
    private String password;

    private static final String EMAIL_REGEXP = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
}
