package com.wes.processdb.web.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaturalGasRequest {
    
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


    // natural gas specific fields
    private Double pressure;
    
    private String gasType;
    private Double HHV;
    private Double LHV;



}
