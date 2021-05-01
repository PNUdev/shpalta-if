package com.pnu.dev.shpaltaif.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TelegramMessage {

    @NotNull
    private String content;

    private boolean shareForAllUsers;

    private Long categoryId;

}
