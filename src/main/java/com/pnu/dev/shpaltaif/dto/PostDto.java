package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @NotBlank(message = "Заголовок не може бути порожнім")
    private String title;

    @NotBlank(message = "Посилання на головне зображення не може бути порожнім")
    private String pictureUrl;

    @NotNull
    private Long categoryId;

    @NotBlank(message = "Стаття не може бути порожньою")
    private String content;

    private boolean active = true;

    private boolean sendTelegramNotifications;

}
