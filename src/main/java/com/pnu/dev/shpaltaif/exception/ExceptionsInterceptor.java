package com.pnu.dev.shpaltaif.exception;

import com.pnu.dev.shpaltaif.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_ERROR;

@ControllerAdvice
public class ExceptionsInterceptor {

    @ExceptionHandler(ServiceAdminException.class)
    public String serviceAdminException(ServiceAdminException serviceException, RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ERROR, serviceException.getMessage());

        return getRedirectUrl(request);
    }

    @ExceptionHandler(Exception.class)
    public String unhandledException(RedirectAttributes redirectAttributes, HttpServletRequest request) {

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ERROR, "Виникла внутрішня помилка сервера");

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
