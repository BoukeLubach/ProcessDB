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
@Table(name = "non_combustible_gases")
@PrimaryKeyJoinColumn(name = "utility_id")
public class NonCombustionGas extends Utility {

    // No id field is declared here since it's inherited from the parent class

    @Column(name = "gas_type")
    private String gasType;

    @Column(name = "pressure")
    private Long pressure;


    // No temperature field for non-combustible gases

    public NonCombustionGas(String name, String gasType) {
        super(name);
        this.gasType = gasType;
    }

}