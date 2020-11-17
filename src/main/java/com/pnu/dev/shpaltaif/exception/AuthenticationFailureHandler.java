package com.pnu.dev.shpaltaif.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_ERROR;

@Component("authenticationFailureHandler")
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException authenticationException) throws IOException, ServletException {
        setDefaultFailureUrl("/login");

        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, authenticationException);

        String errorMessage = "Помилка авторизації";
        if (authenticationException.getMessage().equalsIgnoreCase("Bad credentials")) {
            errorMessage = "Невірні логін або пароль користувача.";
        } else if (authenticationException.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = "Забагато невдалих спроб входу, ваша IP-адреса заблокована на 24 години.";
        }
        httpServletRequest.getSession().setAttribute(FLASH_MESSAGE_ERROR, errorMessage);
    }
}
