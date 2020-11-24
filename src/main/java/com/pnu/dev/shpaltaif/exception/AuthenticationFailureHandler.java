package com.pnu.dev.shpaltaif.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authenticationFailureHandler")
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException authenticationException) throws IOException, ServletException {

        setDefaultFailureUrl("/login?error=" + authenticationException.getMessage());

        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, authenticationException);
    }
}
