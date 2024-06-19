package com.usmobile.useranalytics.repository;

import com.usmobile.useranalytics.repository.BaseIntegrationTest;
import com.usmobile.useranalytics.model.DailyUsage;
import com.usmobile.useranalytics.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DailyUsageRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    @Test
    void testFindByCycleId() {
        String cycleId = "cycle1";
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByCycleId(cycleId);
        assertNotNull(dailyUsages);
        assertEquals(3, dailyUsages.size());
    }

    @Test
    void testFindByUserIdAndMdnAndUsageDate() {
        String userId = "12345";
        String mdn = "2027050505";
        Date usageDate = DateUtils.getTodayMidnight();

        DailyUsage dailyUsage = dailyUsageRepository.findByUserIdAndMdnAndUsageDate(userId, mdn, usageDate);
        assertNotNull(dailyUsage);
        assertEquals("usage6", dailyUsage.getId());
        assertEquals(300.0, dailyUsage.getUsedInMb());
    }
}
