package com.pnu.dev.shpaltaif.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    ROLE_ADMIN("Адміністратор"),
    ROLE_WRITER("Журналіст"),
    ROLE_EDITOR("Головний редактор");

    private final String ukrainianName;

    public String getUkrainianName() {
        return ukrainianName;
    }

}
