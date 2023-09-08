package com.wes.processdb.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wes.processdb.data.entity.utilities.NaturalGas;
import com.wes.processdb.data.entity.Utility;
import com.wes.processdb.data.entity.utilities.Electricity;
import com.wes.processdb.data.entity.utilities.Water;
import com.wes.processdb.data.entity.utilities.NonCombustionGas;

import org.springframework.data.domain.Sort;
import java.util.List;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Long> {

    // @Override
    // List<Utility> findAll(Sort sort);

    @Query("SELECT ng FROM NaturalGas ng")
    List<NaturalGas> findAllNaturalGas(Sort sort);

    @Query("SELECT ng FROM NaturalGas ng")
    List<NaturalGas> findAllNaturalGas();

    @Query("SELECT e FROM Electricity e")
    List<Electricity> findAllElectricity(Sort sort);

    @Query("SELECT e FROM Electricity e")
    List<Electricity> findAllElectricity();

    @Query("SELECT w FROM Water w")
    List<Water> findAllWater(Sort sort);

    @Query("SELECT w FROM Water w")
    List<Water> findAllWater();

    @Query("SELECT ncg FROM NonCombustionGas ncg")
    List<NonCombustionGas> findAllNonCombustionGas(Sort sort);

    @Query("SELECT ncg FROM NonCombustionGas ncg")
    List<NonCombustionGas> findAllNonCombustionGas();

    // @Query("SELECT u FROM Utility u WHERE u.process.id = :process")
    // List<Utility> findUtilitiesByProcessId(@Param("processId") Long processId);

    
    @Query("SELECT ng FROM NaturalGas ng")
    List<Utility> findUtilitiesByProcessId();
}
