package com.usmobile.useranalytics.controller;

import com.usmobile.useranalytics.dto.CycleDTO;
import com.usmobile.useranalytics.service.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cycles")
public class CycleController {

    private final CycleService cycleService;

    @Autowired
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<CycleDTO>> getCycleHistory(@RequestParam String userId, @RequestParam String mdn) {
        List<CycleDTO> cycles = cycleService.getCycleHistory(userId, mdn);
        return ResponseEntity.ok(cycles);
    }

    @GetMapping("/current")
    public ResponseEntity<CycleDTO> getCurrentCycle(@RequestParam String userId, @RequestParam String mdn) {
        CycleDTO currentCycle = cycleService.getCurrentCycle(userId, mdn);
        return ResponseEntity.ok(currentCycle);
    }
}
