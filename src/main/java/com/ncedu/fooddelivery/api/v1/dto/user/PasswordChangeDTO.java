package com.ncedu.fooddelivery.api.v1.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordChangeDTO {

    @NotBlank
    @Size(min=8, max=64)
    private String oldPassword;
    @NotBlank
    @Size(min=8, max=64)
    private String newPassword;
    @NotBlank
    @Size(min=8, max=64)
    private String newPasswordConfirm;
}
