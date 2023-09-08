package com.wes.processdb.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="company")
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @NotBlank(message = "The 'name' field is required.")
    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    
    @Column(name = "description")
    private String description;

    
    @Column(name = "location")    
    private String location;

    @Column(name="latttude")
    private Double latitude;

    @Column(name="longitude")
    private Double longitude;



    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Process> processes;
    
    // @JsonIgnore
    // @OneToMany(mappedBy = "utility", cascade = CascadeType.ALL)
    // private List<Utility> utilities;
    

    public Company(String name, String description, String location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }
    
    public Company(String name) {
       this.name = name;
        
    }
}
