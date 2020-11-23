package com.pnu.dev.shpaltaif.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AuthExceptionMessage {

    BAD_CREDENTIALS("Bad credentials", "Неправильні ім'я користувача або пароль!"),
    IP_BLOCKED("IP blocked", "Забагато невдалих спроб входу, ваша IP-адреса заблокована на 24 години!");

    String value;

    String ukrainianValue;

    public static String translateMessage(String message) {
        for (AuthExceptionMessage authExceptionMessage : values()) {
            if (authExceptionMessage.value.equals(message)) {
                return authExceptionMessage.ukrainianValue;
            }
        }
        return "Помилка авторизації";
    }
}
