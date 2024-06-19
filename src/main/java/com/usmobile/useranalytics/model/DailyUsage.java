package com.usmobile.useranalytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "daily_usage")
public class DailyUsage {
    @Id
    private String id;
    private String mdn;
    private String userId;
    private String cycleId;
    private Date usageDate;
    private Double usedInMb;
}
