package com.pnu.dev.shpaltaif.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
@DirtiesContext(classMode = BEFORE_CLASS)
public abstract class BaseIntegrationTest {

}
