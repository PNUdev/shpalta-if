package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicAccountDto {

    @NotNull(message = "Ім'я повинно бути вказаним")
    @NotBlank
    @Length(min = 3)
    private String name;

    @NotNull(message = "Прізвище повинно бути вказаним")
    @NotBlank
    @Length(min = 3)
    private String surname;

    private String pseudonym;

    private boolean pseudonymUsed;

    private String profileImageUrl;

    private String description;

}
