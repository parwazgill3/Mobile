package com.usmobile.useranalytics.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CycleUsageDTO {
    private Date startDate;
    private Date endDate;
    private Double totalUsage;

    public CycleUsageDTO(Date startDate, Date endDate, Double totalUsage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalUsage = totalUsage;
    }
}
