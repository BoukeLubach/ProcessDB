package com.wes.processdb.web.requests;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NonCombustionGasRequest {

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

    // non combustion gas specific fields
    private String gasType;
    private Long pressure;
    private String composition;

}
