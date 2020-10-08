package com.pnu.dev.shpaltaif.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostFiltersDto {

    private String title;

    private Long authorPublicAccountId;

    private Long categoryId;

    private String categoryUrl;

    private boolean active = true;
}
