package com.wes.processdb.business;


import java.util.List;


import com.wes.processdb.data.entity.Company;


public interface CompanyService {

    // CRUD operations
    Company saveCompany(Company company);
    List<Company> getAllCompanies();


    Company getCompany(Long companyId);
    Company updateCompany(Long companyId, Company company);
    void deleteCompany(Long companyId);


    Company getOrCreateCompany(String name);

}