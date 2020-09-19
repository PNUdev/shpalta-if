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

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String repeatedPassword;

    @NotNull
    private String name;

    @NotNull
    private String surname;

}
