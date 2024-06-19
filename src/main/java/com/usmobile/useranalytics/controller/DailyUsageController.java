package com.usmobile.useranalytics.controller;

import com.usmobile.useranalytics.dto.DailyUsageDTO;
import com.usmobile.useranalytics.dto.CycleUsageDTO;
import com.usmobile.useranalytics.service.DailyUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usage")
public class DailyUsageController {

    private final DailyUsageService dailyUsageService;

    @Autowired
    public DailyUsageController(DailyUsageService dailyUsageService) {
        this.dailyUsageService = dailyUsageService;
    }

    @GetMapping("/current")
    public ResponseEntity<List<DailyUsageDTO>> getCurrentCycleDailyUsage(@RequestParam String userId, @RequestParam String mdn) {
        List<DailyUsageDTO> usageList = dailyUsageService.getCurrentCycleDailyUsage(userId, mdn);
        if (usageList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usageList);
    }

    @GetMapping("/currentDayUsage")
    public ResponseEntity<DailyUsageDTO> getCurrentDayUsage(@RequestParam String userId, @RequestParam String mdn) {
        DailyUsageDTO dailyUsage = dailyUsageService.getCurrentDayUsage(userId, mdn);
        if (dailyUsage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dailyUsage);
    }

    @GetMapping("/total/currentCycle")
    public ResponseEntity<CycleUsageDTO> getTotalDataUsageOfCurrentCycle(@RequestParam String userId, @RequestParam String mdn) {
        CycleUsageDTO totalUsage = dailyUsageService.getTotalDataUsageOfCurrentCycle(userId, mdn);
        if (totalUsage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(totalUsage);
    }

    @GetMapping("/total/history")
    public ResponseEntity<List<CycleUsageDTO>> getTotalDataUsageHistoryForUserAndMdn(@RequestParam String userId, @RequestParam String mdn) {
        List<CycleUsageDTO> usageHistory = dailyUsageService.getTotalDataUsageHistoryForUserAndMdn(userId, mdn);
        if (usageHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usageHistory);
    }
}