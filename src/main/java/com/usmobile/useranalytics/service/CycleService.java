package com.usmobile.useranalytics.service;

import com.usmobile.useranalytics.dto.CycleDTO;
import com.usmobile.useranalytics.model.Cycle;
import com.usmobile.useranalytics.repository.CycleRepository;
import com.usmobile.useranalytics.exception.CycleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CycleService {

    private final CycleRepository cycleRepository;

    @Autowired
    public CycleService(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    public CycleDTO getCurrentCycle(String userId, String mdn) {
        Date nowUtc = Date.from(LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC));
        Optional<Cycle> currentCycle = cycleRepository.findByMdnAndUserIdAndEndDateAfter(mdn, userId, nowUtc);
        return currentCycle.map(CycleDTO::fromCycle)
                .orElseThrow(() -> new CycleNotFoundException("No current cycle found for user: " + userId + " and mdn: " + mdn));
    }

    public List<CycleDTO> getCycleHistory(String userId, String mdn) {
        List<Cycle> cycles = cycleRepository.findByMdnAndUserIdOrderByEndDate(mdn, userId);
        if (cycles.isEmpty()) {
            throw new CycleNotFoundException("No cycles found for user: " + userId + " and mdn: " + mdn);
        }
        return cycles.stream()
                .map(CycleDTO::fromCycle)
                .collect(Collectors.toList());
    }
}
