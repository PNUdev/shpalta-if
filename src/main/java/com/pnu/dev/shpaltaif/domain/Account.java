package com.pnu.dev.shpaltaif.domain;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

public class Account {

    private String name;

    private String surname;

    private String profileImageUrl;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private User user;

}
