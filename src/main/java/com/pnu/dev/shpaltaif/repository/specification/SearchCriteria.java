package com.pnu.dev.shpaltaif.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private final String key;
    private Object value;
    private SearchOperation operation;
}
