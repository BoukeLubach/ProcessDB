package com.wes.processdb.data.repository; 

import com.wes.processdb.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Company findByCompanyId(Long companyId);

    Optional<Company> findByName(String name);
    Boolean existsByName(String name);

    
    @Override
    List<Company> findAll();

    @Transactional
    void deleteById(Long companyId);

    

}
