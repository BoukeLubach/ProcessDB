package com.wes.processdb.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "utility")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "utility_type")
public abstract class Utility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utility_id")
    private Long id;

    @Column(name = "utility_name")
    private String name;

    @Column(name = "utility_description")
    private String description;

    @Column(name = "amuount")
    private Double amount;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "purchased")
    private Boolean purchased;

    @Column(name = "category")
    private String category;
    
    @ManyToOne()
    @JoinColumn(name = "source_id", referencedColumnName = "utility_id")
    private Utility source;

    
    @ManyToOne()
    @JoinColumn(name = "process_id", referencedColumnName = "id")
    private Process process;

    @Transient
    private Long processId;

    @Transient
    private String processName;


    public Utility(String name) {
        this.name = name;
    }



}