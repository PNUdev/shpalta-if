package com.pnu.dev.shpaltaif.dto.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionsInfo {

    private Long subscribedUsersCount;

    private Double percentOfTotalUsersCount;

}
