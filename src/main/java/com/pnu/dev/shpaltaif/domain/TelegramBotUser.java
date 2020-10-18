package com.pnu.dev.shpaltaif.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TelegramBotUser {

    @Id
    private Long chatId;

    @NotNull
    private String settingsToken;

    @NotNull
    @ManyToMany
    @JoinTable(name = "telegram_user_category_subscriptions",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> subscribedCategories;

}
