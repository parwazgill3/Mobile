package com.usmobile.useranalytics.dto;

import com.usmobile.useranalytics.model.DailyUsage;
import lombok.Data;

import java.util.Date;

@Data
public class DailyUsageDTO {
    private Date usageDate;
    private Double usedInMb;

    // Conversion methods
    public static DailyUsageDTO fromDailyUsage(DailyUsage dailyUsage) {
        DailyUsageDTO dto = new DailyUsageDTO();
        dto.setUsageDate(dailyUsage.getUsageDate());
        dto.setUsedInMb(dailyUsage.getUsedInMb());
        return dto;
    }

}
