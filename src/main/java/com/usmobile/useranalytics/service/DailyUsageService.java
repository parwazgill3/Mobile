package com.usmobile.useranalytics.service;

import com.usmobile.useranalytics.dto.CycleDTO;
import com.usmobile.useranalytics.dto.CycleUsageDTO;
import com.usmobile.useranalytics.dto.DailyUsageDTO;
import com.usmobile.useranalytics.exception.DailyUsageNotFoundException;
import com.usmobile.useranalytics.model.DailyUsage;
import com.usmobile.useranalytics.repository.DailyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class DailyUsageService {

    private final DailyUsageRepository dailyUsageRepository;
    private final CycleService cycleService;

    @Autowired
    public DailyUsageService(DailyUsageRepository dailyUsageRepository, CycleService cycleService) {
        this.dailyUsageRepository = dailyUsageRepository;
        this.cycleService = cycleService;
    }

    public List<DailyUsageDTO> getCurrentCycleDailyUsage(String userId, String mdn) {
        CycleDTO currentCycle = cycleService.getCurrentCycle(userId, mdn);
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByCycleId(currentCycle.getId());
        if (dailyUsages.isEmpty()) {
            throw new DailyUsageNotFoundException("No daily usage data found for the current cycle for user: " + userId);
        }
        return dailyUsages.stream().map(DailyUsageDTO::fromDailyUsage).collect(Collectors.toList());
    }

    public DailyUsageDTO getCurrentDayUsage(String userId, String mdn) {
        CycleDTO currentCycle = cycleService.getCurrentCycle(userId, mdn);
        if (currentCycle == null) {
            return null;
        }
        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        Date truncatedDate = Date.from(startOfDay.toInstant(ZoneOffset.UTC));

        DailyUsage dailyUsage = dailyUsageRepository.findByUserIdAndMdnAndUsageDate(userId, mdn, truncatedDate);
        if (dailyUsage == null) {
            throw new DailyUsageNotFoundException("No usage found for today for user: " + userId + " and mdn: " + mdn);
        }
        return DailyUsageDTO.fromDailyUsage(dailyUsage);
    }

    public CycleUsageDTO getTotalDataUsageOfCurrentCycle(String userId, String mdn) {
        CycleDTO currentCycle = cycleService.getCurrentCycle(userId, mdn);
        if (currentCycle == null) {
            return null;
        }
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByCycleId(currentCycle.getId());
        if (dailyUsages.isEmpty()) {
            throw new DailyUsageNotFoundException("No daily usage data found for the current cycle for user: " + userId);
        }
        Double totalUsage = dailyUsages.stream().mapToDouble(DailyUsage::getUsedInMb).sum();
        return new CycleUsageDTO(currentCycle.getStartDate(), currentCycle.getEndDate(), totalUsage);
    }

    public List<CycleUsageDTO> getTotalDataUsageHistoryForUserAndMdn(String userId, String mdn) {
    List<CycleDTO> cycles = cycleService.getCycleHistory(userId, mdn);
    return cycles.stream().map(cycle -> {
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByCycleId(cycle.getId());
        if (dailyUsages.isEmpty()) {
            throw new DailyUsageNotFoundException("No daily usage data found for the current cycle for user: " + userId);
        }

        Double totalUsage = dailyUsages.stream().mapToDouble(DailyUsage::getUsedInMb).sum();
        return new CycleUsageDTO(cycle.getStartDate(), cycle.getEndDate(), totalUsage);
    }).collect(Collectors.toList());
    }
}
