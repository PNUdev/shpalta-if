package com.pnu.dev.shpaltaif.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePasswordDto {

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;

    @NotNull
    private String newPasswordRepeated;

}
