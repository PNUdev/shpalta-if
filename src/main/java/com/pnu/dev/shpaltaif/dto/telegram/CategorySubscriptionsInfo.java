package com.pnu.dev.shpaltaif.dto.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorySubscriptionsInfo {

    private Category category;

    private UserSubscriptionsInfo userSubscriptionsInfo;

}
