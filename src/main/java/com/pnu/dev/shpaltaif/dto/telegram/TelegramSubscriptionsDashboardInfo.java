package com.pnu.dev.shpaltaif.dto.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramSubscriptionsDashboardInfo {

    private Long totalUsersCount;

    private UserSubscriptionsInfo subscribedToAtLeastOneCategory;

    private List<CategorySubscriptionsInfo> subscriptionsInfos;

}
