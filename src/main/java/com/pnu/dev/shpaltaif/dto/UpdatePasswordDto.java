package com.pnu.dev.shpaltaif.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePasswordDto {

    @NotNull(message = "Старий пароль повинен бути вказаним")
    private String oldPassword;

    @NotNull(message = "Новий пароль повинен бути вказаним")
    private String newPassword;

    @NotNull(message = "Повторення нового паролю повинне бути вказаним")
    private String newPasswordRepeated;

}
