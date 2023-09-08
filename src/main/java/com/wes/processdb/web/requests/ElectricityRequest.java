package com.wes.processdb.web.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityRequest {

    private String category;
    
    // common fields for all utilities
    private String name;
    private Double price;
    private Double amount;
    private String unitOfMeasurement;
    private Boolean purchased;
    private Long sourceId;
    
    // field to search for process 
    private Long processId;
    private String processName;
    

    // electricity specific fields
    private Double voltage;
    private Double frequency;
    private String AC_or_DC;

   
}
