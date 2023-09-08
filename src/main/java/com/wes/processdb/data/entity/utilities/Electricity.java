package com.wes.processdb.data.entity.utilities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.wes.processdb.data.entity.Utility;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "electricity")
@PrimaryKeyJoinColumn(name = "utility_id")
public class Electricity extends Utility {

    // No id field is declared here since it's inherited from the parent class

    @Column(name = "voltage")
    private Double voltage;

    @Column(name = "frequency")
    private Double frequency;


    @Column(name = "AC_or_DC")
    private String AC_or_DC;


    public Electricity(String name, Double voltage) {
        super(name);
        this.voltage = voltage;
    }   
}