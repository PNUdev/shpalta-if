package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicAccountDto {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String profileImageUrl;

    private String description;

}
