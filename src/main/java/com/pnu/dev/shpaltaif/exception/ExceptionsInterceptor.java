package com.pnu.dev.shpaltaif.exception;

import com.pnu.dev.shpaltaif.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_ERROR;

@ControllerAdvice
@Slf4j
public class ExceptionsInterceptor {

    @ExceptionHandler(ServiceException.class)
    public String serviceAdminException(ServiceException serviceException, RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {

        log.error("ServiceException was thrown, httpServletRequest: {}", request, serviceException);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ERROR, serviceException.getMessage());

        return getRedirectUrl(request);
    }

    @ExceptionHandler(Exception.class)
    public String unhandledException(Exception e, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        log.error("Unexpected exception was thrown, httpServletRequest: {}", request, e);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ERROR, "Виникла внутрішня помилка сервера");

        return getRedirectUrl(request);
    }

    @ExceptionHandler(BindException.class)
    public String constraintViolationException(BindException exception,
                                               RedirectAttributes redirectAttributes, HttpServletRequest request) {

        log.error("Constraint violation, httpServletRequest: {}", request, exception);

        String message = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" ;"));

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ERROR, message);

        return getRedirectUrl(request);
    }

    private String getRedirectUrl(HttpServletRequest request) {

        if (StringUtils.equals(request.getMethod(), "POST")) {
            return "redirect:" + HttpUtils.getPreviousPageUrl(request);
        }

        if (StringUtils.startsWith(request.getRequestURI(), "/admin")) {
            return "redirect:/admin";
        }

        return "redirect:/";

    }

}
