package com.usmobile.useranalytics.service;

import com.usmobile.useranalytics.dto.DailyUsageDTO;
import com.usmobile.useranalytics.dto.CycleUsageDTO;
import com.usmobile.useranalytics.dto.CycleDTO;
import com.usmobile.useranalytics.exception.DailyUsageNotFoundException;
import com.usmobile.useranalytics.model.Cycle;
import com.usmobile.useranalytics.model.DailyUsage;
import com.usmobile.useranalytics.repository.DailyUsageRepository;
import com.usmobile.useranalytics.util.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailyUsageServiceTest {

    @Mock
    private DailyUsageRepository dailyUsageRepository;

    @Mock
    private CycleService cycleService;

    @InjectMocks
    private DailyUsageService dailyUsageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentCycleDailyUsage_success() {
        String userId = "12345";
        String mdn = "2027050505";
        Date startDate = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate = DateUtils.getDateDaysFromNowEndOfDay(26);

        Cycle currentCycle = new Cycle("cycle2", mdn, startDate, endDate, userId);

        DailyUsage usage1 = new DailyUsage("usage1", mdn, userId, "cycle2", DateUtils.getDateDaysAgoMidnight(2), 100.0);
        DailyUsage usage2 = new DailyUsage("usage2", mdn, userId, "cycle2", DateUtils.getDateDaysAgoMidnight(1), 200.0);
        DailyUsage usage3 = new DailyUsage("usage3", mdn, userId, "cycle2", DateUtils.getTodayMidnight(), 300.0);

        when(cycleService.getCurrentCycle(userId, mdn)).thenReturn(CycleDTO.fromCycle(currentCycle));
        when(dailyUsageRepository.findByCycleId(currentCycle.getId())).thenReturn(List.of(usage1, usage2, usage3));

        List<DailyUsageDTO> result = dailyUsageService.getCurrentCycleDailyUsage(userId, mdn);

        assertEquals(3, result.size());
        assertEquals(100.0, result.get(0).getUsedInMb());
        assertEquals(200.0, result.get(1).getUsedInMb());
        assertEquals(300.0, result.get(2).getUsedInMb());
    }

    @Test
    void getCurrentCycleDailyUsage_notFound() {
        String userId = "12345";
        String mdn = "2027050505";

        CycleDTO currentCycle = new CycleDTO();
        currentCycle.setId("cycle1");
        currentCycle.setStartDate(DateUtils.getDateDaysAgoMidnight(3));
        currentCycle.setEndDate(DateUtils.getDateDaysFromNowEndOfDay(26));

        when(cycleService.getCurrentCycle(userId, mdn)).thenReturn(currentCycle);
        when(dailyUsageRepository.findByCycleId("cycle1")).thenReturn(List.of());

        assertThrows(DailyUsageNotFoundException.class, () -> {
            dailyUsageService.getCurrentCycleDailyUsage(userId, mdn);
        });
    }

    @Test
    void getCurrentDayUsage_success() {
        String userId = "12345";
        String mdn = "2027050505";
        Date startDate = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate = DateUtils.getDateDaysFromNowEndOfDay(26);

        Cycle currentCycle = new Cycle("cycle2", mdn, startDate, endDate, userId);

        DailyUsage usage = new DailyUsage("usage3", mdn, userId, "cycle2", DateUtils.getTodayMidnight(), 300.0);

        when(cycleService.getCurrentCycle(userId, mdn)).thenReturn(CycleDTO.fromCycle(currentCycle));
        when(dailyUsageRepository.findByUserIdAndMdnAndUsageDate(userId, mdn, DateUtils.getTodayMidnight())).thenReturn(usage);

        DailyUsageDTO result = dailyUsageService.getCurrentDayUsage(userId, mdn);

        assertNotNull(result);
        assertEquals(300.0, result.getUsedInMb());
    }

    @Test
    void getCurrentDayUsage_notFound() {
        String userId = "12345";
        String mdn = "2027050505";
        Date startDate = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate = DateUtils.getDateDaysFromNowEndOfDay(26);

        Cycle currentCycle = new Cycle("cycle2", mdn, startDate, endDate, userId);

        when(cycleService.getCurrentCycle(userId, mdn)).thenReturn(CycleDTO.fromCycle(currentCycle));
        when(dailyUsageRepository.findByUserIdAndMdnAndUsageDate(userId, mdn, DateUtils.getTodayMidnight())).thenReturn(null);

        assertThrows(DailyUsageNotFoundException.class, () -> {
            dailyUsageService.getCurrentDayUsage(userId, mdn);
        });

        verify(dailyUsageRepository).findByUserIdAndMdnAndUsageDate(userId, mdn, DateUtils.getTodayMidnight());
    }

    @Test
    void getTotalDataUsageOfCurrentCycle_success() {
        String userId = "12345";
        String mdn = "2027050505";
        Date startDate = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate = DateUtils.getDateDaysFromNowEndOfDay(26);

        Cycle currentCycle = new Cycle("cycle2", mdn, startDate, endDate, userId);

        DailyUsage usage1 = new DailyUsage("usage1", mdn, userId, "cycle2", DateUtils.getDateDaysAgoMidnight(2), 100.0);
        DailyUsage usage2 = new DailyUsage("usage2", mdn, userId, "cycle2", DateUtils.getDateDaysAgoMidnight(1), 200.0);
        DailyUsage usage3 = new DailyUsage("usage3", mdn, userId, "cycle2", DateUtils.getTodayMidnight(), 300.0);

        when(cycleService.getCurrentCycle(userId, mdn)).thenReturn(CycleDTO.fromCycle(currentCycle));
        when(dailyUsageRepository.findByCycleId(currentCycle.getId())).thenReturn(List.of(usage1, usage2, usage3));

        CycleUsageDTO result = dailyUsageService.getTotalDataUsageOfCurrentCycle(userId, mdn);

        assertNotNull(result);
        assertEquals(600.0, result.getTotalUsage());
    }

    @Test
    void getTotalDataUsageOfCurrentCycle_notFound() {
        String userId = "12345";
        String mdn = "2027050505";

        CycleDTO currentCycle = new CycleDTO();
        currentCycle.setId("cycle1");
        currentCycle.setStartDate(DateUtils.getDateDaysAgoMidnight(3));
        currentCycle.setEndDate(DateUtils.getDateDaysFromNowEndOfDay(26));

        when(cycleService.getCurrentCycle(userId, mdn)).thenReturn(currentCycle);
        when(dailyUsageRepository.findByCycleId("cycle1")).thenReturn(List.of());

        assertThrows(DailyUsageNotFoundException.class, () -> {
            dailyUsageService.getTotalDataUsageOfCurrentCycle(userId, mdn);
        });
    }

    @Test
    void getTotalDataUsageHistoryForUserAndMdn_success() {
        String userId = "12345";
        String mdn = "2027050505";
        Date startDate1 = DateUtils.getDateDaysAgoMidnight(33);
        Date endDate1 = DateUtils.getDateDaysAgoEndOfDay(4);
        Date startDate2 = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate2 = DateUtils.getDateDaysFromNowEndOfDay(26);

        Cycle cycle1 = new Cycle("cycle1", mdn, startDate1, endDate1, userId);
        Cycle cycle2 = new Cycle("cycle2", mdn, startDate2, endDate2, userId);

        DailyUsage usage1 = new DailyUsage("usage1", mdn, userId, "cycle1", DateUtils.getDateDaysAgoMidnight(6), 100.0);
        DailyUsage usage2 = new DailyUsage("usage2", mdn, userId, "cycle1", DateUtils.getDateDaysAgoMidnight(5), 200.0);
        DailyUsage usage3 = new DailyUsage("usage3", mdn, userId, "cycle1", DateUtils.getDateDaysAgoMidnight(4), 300.0);

        DailyUsage usage4 = new DailyUsage("usage4", mdn, userId, "cycle2", DateUtils.getDateDaysAgoMidnight(2), 100.0);
        DailyUsage usage5 = new DailyUsage("usage5", mdn, userId, "cycle2", DateUtils.getDateDaysAgoMidnight(1), 200.0);
        DailyUsage usage6 = new DailyUsage("usage6", mdn, userId, "cycle2", DateUtils.getTodayMidnight(), 300.0);

        when(cycleService.getCycleHistory(userId, mdn)).thenReturn(List.of(CycleDTO.fromCycle(cycle1), CycleDTO.fromCycle(cycle2)));
        when(dailyUsageRepository.findByCycleId("cycle1")).thenReturn(List.of(usage1, usage2, usage3));
        when(dailyUsageRepository.findByCycleId("cycle2")).thenReturn(List.of(usage4, usage5, usage6));

        List<CycleUsageDTO> result = dailyUsageService.getTotalDataUsageHistoryForUserAndMdn(userId, mdn);

        assertEquals(2, result.size());
        assertEquals(600.0, result.get(0).getTotalUsage());
        assertEquals(600.0, result.get(1).getTotalUsage());
    }

    @Test
    void getTotalDataUsageHistoryForUserAndMdn_notFound() {
        String userId = "12345";
        String mdn = "2027050505";

        CycleDTO cycle1 = new CycleDTO();
        cycle1.setId("cycle1");
        cycle1.setStartDate(DateUtils.getDateDaysAgoMidnight(33));
        cycle1.setEndDate(DateUtils.getDateDaysAgoEndOfDay(4));

        CycleDTO cycle2 = new CycleDTO();
        cycle2.setId("cycle2");
        cycle2.setStartDate(DateUtils.getDateDaysAgoMidnight(3));
        cycle2.setEndDate(DateUtils.getDateDaysFromNowEndOfDay(26));

        when(cycleService.getCycleHistory(userId, mdn)).thenReturn(List.of(cycle1, cycle2));
        when(dailyUsageRepository.findByCycleId("cycle1")).thenReturn(List.of());
        when(dailyUsageRepository.findByCycleId("cycle2")).thenReturn(List.of());

        assertThrows(DailyUsageNotFoundException.class, () -> {
            dailyUsageService.getTotalDataUsageHistoryForUserAndMdn(userId, mdn);
        });
    }
}
