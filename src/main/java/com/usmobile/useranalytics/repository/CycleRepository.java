package com.usmobile.useranalytics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.usmobile.useranalytics.model.Cycle;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.Optional;
import java.util.List;

public interface CycleRepository extends MongoRepository<Cycle, String> {
    @Query("{'mdn': ?0, 'userId': ?1, 'endDate': {$gte: ?2}}")
    Optional<Cycle> findByMdnAndUserIdAndEndDateAfter(String mdn, String userId, Date date);
    List<Cycle> findByMdnAndUserIdOrderByEndDate(String mdn, String userId);
}
