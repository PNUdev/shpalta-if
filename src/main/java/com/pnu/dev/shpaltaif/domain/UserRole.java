package com.pnu.dev.shpaltaif.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    ROLE_ADMIN("Адмін"),
    ROLE_WRITER("Журналіст"),
    ROLE_EDITOR("Головний редактор");

    private final String ukrainianName;

    public String getUkrainianName() {
        return ukrainianName;
    }

    public static UserRole[] getAvailableRolesForNewUser() {
        return new UserRole[]{ROLE_WRITER, ROLE_EDITOR};
    }
}
