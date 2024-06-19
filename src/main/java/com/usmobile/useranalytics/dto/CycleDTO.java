package com.usmobile.useranalytics.dto;

import lombok.Data;

import com.usmobile.useranalytics.model.Cycle;
import java.util.Date;

@Data
public class CycleDTO {
    private String id;
    private Date startDate;
    private Date endDate;

    public static CycleDTO fromCycle(Cycle cycle) {
        CycleDTO responseDTO = new CycleDTO();
        responseDTO.setId(cycle.getId());
        responseDTO.setStartDate(cycle.getStartDate());
        responseDTO.setEndDate(cycle.getEndDate());
        return responseDTO;
    }
}
