package com.pnu.dev.shpaltaif.dto;

import com.pnu.dev.shpaltaif.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUserCategorySubscription {

    private Category category;

    private boolean subscribed;

}
