package com.pnu.dev.shpaltaif.service;

public interface AuthSessionSynchronizer {

    void refreshPrincipalInAuthSession(Long userId);

}
