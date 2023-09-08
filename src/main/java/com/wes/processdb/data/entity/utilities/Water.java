package com.wes.processdb.data.entity.utilities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.wes.processdb.data.entity.Utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "condensate")
@PrimaryKeyJoinColumn(name = "utility_id")
public class Water extends Utility {

    // No id field is declared here since it's inherited from the parent class

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "quality")
    private String quality;

    @Column(name = "purpose")
    private String purpose;
    

}