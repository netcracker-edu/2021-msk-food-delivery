package com.ncedu.fooddelivery.api.v1.dto.user;

import com.ncedu.fooddelivery.api.v1.validators.EnumValid;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.UUID;

@Data
public class NewUserDTO {
    //TODO: Class for converting phoneNumber in required format

    //common user info
    @NotBlank
    @EnumValid(
          enumClazz = com.ncedu.fooddelivery.api.v1.entities.Role.class,
          message = "Role is not valid"
    )
    private String role;
    @NotBlank
    @Size(min=8, max=64)
    private String password;
    @NotBlank
    @Size(min=6, max=50)
    private String fullName;
    @NotBlank
    @Email(regexp = EMAIL_REGEXP)
    private String email;
    private UUID avatarId;

    //client additional fields
    private String paymentData;
    @Size(min=11, max=20)
    private String phoneNumber;
    @Min(value = 0)
    @Max(value = 5)
    private Double rating;

    //moderator additional fields
    @Min(value = 1)
    private Long warehouseId;

    private static final String EMAIL_REGEXP = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
}
