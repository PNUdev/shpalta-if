package com.pnu.dev.shpaltaif.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class HeaderLinkUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String link;

    private boolean openInNewTab;

}
