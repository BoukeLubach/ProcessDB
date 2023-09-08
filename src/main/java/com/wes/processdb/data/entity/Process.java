package com.wes.processdb.data.entity;

import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.NonNull;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "process")
public class Process {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @NotNull
    @Column(name = "name")
    private String name;

    
    @Column(name = "description")
    private String description;

    @Column(name = "product")
    private String product;


    @Column(name = "max_process_temperature")
    private Double maxProcessTemperature;

    @Column(name = "raw_material")
    private String rawMaterial;

    @Column(name = "power")
    private Double power;

    @Column(name = "operating_hours")
    private Integer operatingHours;

    
    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_process_id")
    private Process parentProcess;

    @OneToMany(mappedBy = "parentProcess")
    private List<Process> childProcesses;
    
    @Transient
    private Long companyId;

    @Transient
    private String companyName;

    @Transient
    private Integer annualConsumption;

    public Long getCompanyId() {
        if (company != null) {
            return company.getId();
        }
        return null;
    }

    public String getCompanyName() {
        if (company != null) {
            return company.getName();
        }
        return null;
    }

    public Double getAnnualConsumption() {
        if (power == null || operatingHours == null) {
            return 0.0;
        }
        return power * operatingHours;
    }

    // add a constructor to initialize all fields
    public Process(String name, String description, String product, Double maxProcessTemperature, String rawMaterial, Double power, Integer operatingHours, Company company) {
        this.name = name;
        this.description = description;
        this.product = product;
        this.maxProcessTemperature = maxProcessTemperature;
        this.rawMaterial = rawMaterial;
        this.power = power;
        this.operatingHours = operatingHours;
        this.company = company;
    }


    // add a constructor to initialize name
    public Process(String name) {
       this.name = name;
        
    }
}
