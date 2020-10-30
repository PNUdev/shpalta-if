package com.pnu.dev.shpaltaif.integration;

import com.pnu.dev.shpaltaif.service.telegram.TelegramBot;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@SpringBootTest
@Transactional
@Rollback
@DirtiesContext(classMode = BEFORE_CLASS)
public abstract class BaseIntegrationTest {

    @MockBean
    TelegramBot telegramBot; // no integration test should use real object of this class

}
