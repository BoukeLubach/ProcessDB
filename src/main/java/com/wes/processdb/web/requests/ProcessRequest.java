package com.wes.processdb.web.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequest {

    @NotNull
    @NotBlank(message = "The 'name' field is required")
    private String name;

    private String description;

    private String companyName;

    private Long companyId;

    private String product;

    private String rawMaterial;

    private Double maxProcessTemperature;

    private Double exitProcessTemperature;

    private Double power;

    private Integer operatingHours;

    // constructor with companyname
    public ProcessRequest(String name, String description, String companyName, String product,  Double maxProcessTemperature, Double exitProcessTemperature, Double power,
            int operatingHours) {
        this.name = name;
        this.description = description;
        this.companyName = companyName;
        this.product = product;

        this.maxProcessTemperature = maxProcessTemperature;
        this.exitProcessTemperature = exitProcessTemperature;
        this.power = power;
        this.operatingHours = operatingHours;
    }

    // constructor with companyid
    public ProcessRequest(String name, String description, Long companyId, String product,  Double maxProcessTemperature, Double exitProcessTemperature, Double power,
            int operatingHours) {
        this.name = name;
        this.description = description;
        this.companyId = companyId;
        this.product = product;

        this.maxProcessTemperature = maxProcessTemperature;
        this.exitProcessTemperature = exitProcessTemperature;
        this.power = power;
        this.operatingHours = operatingHours;
    }
}
