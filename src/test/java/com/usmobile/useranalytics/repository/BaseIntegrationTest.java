package com.usmobile.useranalytics.repository;

import com.usmobile.useranalytics.UserAnalyticsApplication;
import com.usmobile.useranalytics.config.DataInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@ContextConfiguration(classes = {UserAnalyticsApplication.class, DataInitializer.class})
public abstract class BaseIntegrationTest {

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        dataInitializer.initializeData();
    }
}

