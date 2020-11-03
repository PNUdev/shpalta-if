package com.pnu.dev.shpaltaif.util;

import com.pnu.dev.shpaltaif.exception.ServiceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Component
@Slf4j
public class FreemarkerTemplateResolver {

    private Configuration configuration;

    @Autowired
    public FreemarkerTemplateResolver(Configuration configuration) {
        this.configuration = configuration;
    }

    public String resolve(String templateName, Map<String, Object> data) {

        try {
            Template template = configuration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (Exception e) {
            log.error("Error while resolving template", e);
            throw new ServiceException("Внутрішня помилка сервера");
        }
    }

}
