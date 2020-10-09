package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotBlank(message = "Назва категорії не може бути порожньою")
    private String title;

    @NotBlank(message = "URL категорії не може бути порожнім")
    private String publicUrl;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Колір має бути валідним")
    private String colorTheme;

}
