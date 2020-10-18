package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicAccountDto {

    @NotBlank(message = "Ім'я повинно бути вказаним")
    @Length(min = 3)
    private String name;

    @NotBlank(message = "Прізвище повинно бути вказаним")
    @Length(min = 3)
    private String surname;

    private String pseudonym;

    private boolean pseudonymUsed;

    private String profileImageUrl;

    private String description;

}
