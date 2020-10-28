package com.pnu.dev.shpaltaif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import static java.util.Collections.singletonList;

@SpringBootApplication
@EnableAsync
public class ShpaltaIfApplication {

    public ShpaltaIfApplication(FreeMarkerConfigurer freeMarkerConfigurer) {
        freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(singletonList("/META-INF/security.tld"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ShpaltaIfApplication.class, args);
    }

}
