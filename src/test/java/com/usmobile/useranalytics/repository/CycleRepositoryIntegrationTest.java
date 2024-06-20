package com.usmobile.useranalytics.repository;

import com.usmobile.useranalytics.model.Cycle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CycleRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CycleRepository cycleRepository;

    @Test
    void testGetCurrentCycle() {
        // Arrange
        String userId = "12345";
        String mdn = "2027050505";

        // Act
        Optional<Cycle> foundCycle = cycleRepository.findByMdnAndUserIdAndEndDateAfter(mdn, userId, new Date());

        // Assert
        assertTrue(foundCycle.isPresent());
        assertEquals("cycle2", foundCycle.get().getId());
    }

    @Test
    void testGetCurrentCycleEndingToday() {
        // Arrange
        String userId = "67890";
        String mdn = "3037050505";

        // Act
        Optional<Cycle> foundCycle = cycleRepository.findByMdnAndUserIdAndEndDateAfter(mdn, userId, new Date());

        // Assert
        assertTrue(foundCycle.isPresent());
        assertEquals("cycle3", foundCycle.get().getId());
    }

    @Test
    void testGetCycleHistory() {
        // Arrange
        String userId = "12345";
        String mdn = "2027050505";

        // Act
        List<Cycle> cycleHistory = cycleRepository.findByMdnAndUserIdOrderByEndDate(mdn, userId);

        // Assert
        assertEquals(2, cycleHistory.size());
        assertEquals("cycle1", cycleHistory.get(0).getId());
        assertEquals("cycle2", cycleHistory.get(1).getId());
    }
}
