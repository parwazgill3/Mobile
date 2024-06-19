package com.usmobile.useranalytics.repository;

import com.usmobile.useranalytics.model.DailyUsage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface DailyUsageRepository extends MongoRepository<DailyUsage, String> {
    List<DailyUsage> findByCycleId(String cycleId);
    DailyUsage findByUserIdAndMdnAndUsageDate(String userId, String mdn, Date usageDate);
}
