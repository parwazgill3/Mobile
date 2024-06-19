package com.usmobile.useranalytics.service;

import com.usmobile.useranalytics.dto.CycleDTO;
import com.usmobile.useranalytics.exception.CycleNotFoundException;
import com.usmobile.useranalytics.model.Cycle;
import com.usmobile.useranalytics.repository.CycleRepository;
import com.usmobile.useranalytics.util.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CycleServiceTest {

    @Mock
    private CycleRepository cycleRepository;

    @InjectMocks
    private CycleService cycleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentCycle_success() {
        // Arrange
        String userId = "12345";
        String mdn = "2027050505";
        Date startDate = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate = DateUtils.getDateDaysFromNowEndOfDay(26);
        Cycle cycle = new Cycle("cycle2", mdn, startDate, endDate, userId);

        when(cycleRepository.findByMdnAndUserIdAndEndDateAfter(anyString(), anyString(), any(Date.class)))
                .thenReturn(Optional.of(cycle));

        // Act
        CycleDTO result = cycleService.getCurrentCycle(userId, mdn);

        // Assert
        assertNotNull(result);
        assertEquals("cycle2", result.getId());
    }

    @Test
    void getCurrentCycle_notFound() {
        // Arrange
        when(cycleRepository.findByMdnAndUserIdAndEndDateAfter(anyString(), anyString(), any(Date.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CycleNotFoundException.class, () -> {
            cycleService.getCurrentCycle("12345", "2027050505");
        });
    }

    @Test
    void getCycleHistory_success() {
        // Arrange
        String userId = "12345";
        String mdn = "2027050505";

        Date startDate1 = DateUtils.getDateDaysAgoMidnight(33);
        Date endDate1 = DateUtils.getDateDaysAgoEndOfDay(4);
        Date startDate2 = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate2 = DateUtils.getDateDaysFromNowEndOfDay(26);
        Cycle cycle1 = new Cycle("cycle1", mdn, startDate1, endDate1, userId);
        Cycle cycle2 = new Cycle("cycle2", mdn, startDate2, endDate2, userId);

        when(cycleRepository.findByMdnAndUserIdOrderByEndDate(anyString(), anyString()))
                .thenReturn(Arrays.asList(cycle1, cycle2));

        // Act
        List<CycleDTO> result = cycleService.getCycleHistory(userId, mdn);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("cycle1", result.get(0).getId());
        assertEquals("cycle2", result.get(1).getId());
    }

    @Test
    void getCycleHistory_notFound() {
        // Arrange
        when(cycleRepository.findByMdnAndUserIdOrderByEndDate(anyString(), anyString()))
                .thenReturn(List.of());

        // Act & Assert
        assertThrows(CycleNotFoundException.class, () -> {
            cycleService.getCycleHistory("12345", "2027050505");
        });
    }
}
