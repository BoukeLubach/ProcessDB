package com.wes.processdb.data.repository;

import com.wes.processdb.data.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    Optional<Process> findByName(String name);
    Boolean existsByName(String name);

    @Override
    List<Process> findAll();

    @Transactional
    void deleteById(Long processId);

    // @Query("SELECT SUM(u.power * u.operating_hours) FROM Process u")
    // Long getTotalAnnualConsumption();

}
