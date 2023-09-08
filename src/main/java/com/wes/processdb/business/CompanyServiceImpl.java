package com.wes.processdb.business;

import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.repository.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wes.processdb.business.exception.ResourceNotFoundException;
import com.wes.processdb.business.exception.DuplicateFieldException;

import java.util.List;
import java.util.Optional;

import lombok.*;

@AllArgsConstructor
// @NoArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    // create a company
    @Override
    public Company saveCompany(Company company) {
        if (companyRepository.existsByName(company.getName())) {
            throw new DuplicateFieldException("A company with the given name already exists");
        }
        companyRepository.save(company);
        return companyRepository.save(company);
    }


    // read all companies
    @Override
    public List<Company> getAllCompanies() {
        return (List<Company>) companyRepository.findAll();
    }


    // read single company
    @Override
    public Company getCompany(Long companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        return unwrapCompany(company, companyId);
    }

    // update a company
    @Override
    public Company updateCompany(Long companyId, Company companyDataToUpdate) {

        Optional<Company> company = companyRepository.findById(companyId);
        Company companyToUpdate = unwrapCompany(company, companyId);
        companyToUpdate.setName(companyDataToUpdate.getName());
        companyToUpdate.setDescription(companyDataToUpdate.getDescription());
        companyToUpdate.setLocation(companyDataToUpdate.getLocation());
        companyToUpdate.setLatitude(companyDataToUpdate.getLatitude());
        companyToUpdate.setLongitude(companyDataToUpdate.getLongitude());
        return companyRepository.save(companyToUpdate);
    }

    // delete a company
    @Override
    public void deleteCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId);
        } else {
            companyRepository.deleteById(companyId);
        }
    }


    // get a company by name, or create it if it does not exist
    @Override
    public Company getOrCreateCompany(String name) {
        Optional<Company> mightExistCompany = companyRepository.findByName(name);

        // if company exists, return it
        if (!mightExistCompany.isEmpty()) {
            return mightExistCompany.get();
        }
    
        // otherwise, create it
        Company newCompany = new Company(name);
        return companyRepository.save(newCompany);
    }



    // helper method to unwrap company from optional
    static Company unwrapCompany(Optional<Company> company, Long companyId) {

        // if company does not exist, throw exception
        if (company.isEmpty()) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId);
        }

        // otherwise, return company
        return company.get();
    }

}
