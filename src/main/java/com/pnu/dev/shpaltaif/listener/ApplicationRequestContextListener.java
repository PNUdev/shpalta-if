package com.pnu.dev.shpaltaif.listener;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.annotation.WebListener;

@Configuration
@WebListener
public class ApplicationRequestContextListener extends RequestContextListener {
}
