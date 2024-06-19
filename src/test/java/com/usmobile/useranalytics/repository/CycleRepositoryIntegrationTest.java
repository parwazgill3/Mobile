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






//package com.usmobile.useranalytics.repository;
//
//import com.usmobile.useranalytics.model.Cycle;
//import com.usmobile.useranalytics.util.DateUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@DataMongoTest
//@ContextConfiguration
//class CycleRepositoryIntegrationTest {
//
//    @Autowired
//    private CycleRepository cycleRepository;
//
//    @BeforeEach
//    void setUp() {
//        cycleRepository.deleteAll();
//    }
//
//    @Test
//    void testGetCurrentCycle() {
//        // Arrange
//        String userId = "12345";
//        String mdn = "2027050505";
//        Date startDate = DateUtils.getDateDaysAgoMidnight(3);
//        Date endDate = DateUtils.getDateDaysFromNowEndOfDay(26);
//        Cycle cycle = new Cycle("cycle2", mdn, startDate, endDate, userId);
//        cycleRepository.save(cycle);
//
//        // Act
//        Optional<Cycle> foundCycle = cycleRepository.findByMdnAndUserIdAndEndDateAfter(mdn, userId, new Date());
//
//        // Assert
//        assertTrue(foundCycle.isPresent());
//        assertEquals("cycle2", foundCycle.get().getId());
//    }
//
//    @Test
//    void testGetCycleHistory() {
//        // Arrange
//        String userId = "12345";
//        String mdn = "2027050505";
//        Date startDate1 = DateUtils.getDateDaysAgoMidnight(33);
//        Date endDate1 = DateUtils.getDateDaysAgoEndOfDay(4);
//        Date startDate2 = DateUtils.getDateDaysAgoMidnight(3);
//        Date endDate2 = DateUtils.getDateDaysFromNowEndOfDay(26);
//        Cycle cycle1 = new Cycle("cycle1", mdn, startDate1, endDate1, userId);
//        Cycle cycle2 = new Cycle("cycle2", mdn, startDate2, endDate2, userId);
//        cycleRepository.save(cycle1);
//        cycleRepository.save(cycle2);
//
//        // Act
//        List<Cycle> foundCycles = cycleRepository.findByMdnAndUserIdOrderByEndDate(mdn, userId);
//
//        // Assert
//        assertEquals(2, foundCycles.size());
//        assertEquals("cycle1", foundCycles.get(0).getId());
//        assertEquals("cycle2", foundCycles.get(1).getId());
//    }
//}
