package com.wes.processdb.data.entity.utilities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.wes.processdb.data.entity.Utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "natural_gas")
@PrimaryKeyJoinColumn(name = "utility_id")
public class NaturalGas extends Utility {

    // No id field is declared here since it's inherited from the parent class

    @Column(name = "pressure")
    private Double pressure;

    @Column(name = "gas_type")
    private String gasType;


    @Column(name = "HHV")
    private Double HHV;

    @Column(name = "LHV")
    private Double LHV;


    public NaturalGas(String name, Double pressure) {
        super(name);
        this.pressure = pressure;
    }

}