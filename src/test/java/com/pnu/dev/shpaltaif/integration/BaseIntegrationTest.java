package com.pnu.dev.shpaltaif.integration;

import com.pnu.dev.shpaltaif.service.telegram.TelegramBot;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @MockBean
    TelegramBot telegramBot; // no integration test should use real object of this class

}
