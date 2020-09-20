package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    @NotNull(message = "Ім'я користувача повинно бути вказаним")
    private String username;

    @NotNull(message = "Пароль повинен бути вказаним")
    private String password;

    @NotNull(message = "Повторення паролю повинно бути вказаним")
    private String repeatedPassword;

    @NotNull(message = "Ім'я повинно бути вказаним")
    private String name;

    @NotNull(message = "Прізвище повинно бути вказаним")
    private String surname;

}
