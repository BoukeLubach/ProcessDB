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
@Table(name = "steam")
@PrimaryKeyJoinColumn(name = "utility_id")
public class Steam extends Utility {

    // No id field is declared here since it's inherited from the parent class

    @Column(name = "temperature")
    private Long temperature;

    @Column(name = "pressure")
    private Long pressure;

    @Column(name = "gross_energy_content")
    private Long grossEnergyContent;

    @Column(name = "net_energy_content")
    private Long netEnergyContent;

    @Column(name = "condensate_returned")
    private Boolean condensateReturned;


    public Steam(String name, Long temperature) {
        super(name);
        this.temperature = temperature;
    }

    // getters and setters for temperatureRating

    // other steam-specific methods go here

    // set net energy content equal to gross if condensate is not returned
    public void setNetEnergyContent() {
        if (this.condensateReturned == false) {
            this.netEnergyContent = this.grossEnergyContent;
        }
    }
}