package com.pnu.dev.shpaltaif.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostsPublicFilter {

    private String title;

    private String categoryUrl;

}
