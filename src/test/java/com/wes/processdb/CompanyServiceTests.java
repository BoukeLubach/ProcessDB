package com.wes.processdb;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wes.processdb.business.exception.ResourceNotFoundException;
import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.repository.CompanyRepository;
import com.wes.processdb.business.CompanyServiceImpl;
import com.wes.processdb.business.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.junit.jupiter.MockitoExtension;


@SpringBootTest
@ActiveProfiles("test")
public class CompanyServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    // private CompanyService companyService;

    private Company company1;
    private Company company2;
    private Company newToBeCreatedCompany; 
    private List<Company> companies; 

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        company1 = new Company();
        company1.setName("Company 1");
        company1.setId(1L);
        company1.setDescription("Test description");
        company1.setLocation("Test location");

        company2 = new Company();
        company2.setName("Company 2");
        company2.setId(2L);
        company2.setDescription("Test description 2");
        company2.setLocation("Test location 2");

        newToBeCreatedCompany = new Company();
        newToBeCreatedCompany.setName("New Company");

        companies = Arrays.asList(company1, company2);
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company1));
        when(companyRepository.findById(2L)).thenReturn(Optional.of(company2));
        when(companyRepository.findById(3L)).thenReturn(Optional.empty());

        when(companyRepository.save(company1)).thenReturn(company1);
        when(companyRepository.save(company2)).thenReturn(company2);
       
      
        when(companyRepository.findAll()).thenReturn(companies);
        when(companyRepository.findByName("Company 1")).thenReturn(Optional.of(company1));
        when(companyRepository.findByName("Company 2")).thenReturn(Optional.of(company2));
     

        when(companyRepository.existsById(1L)).thenReturn(true);
        when(companyRepository.existsById(2L)).thenReturn(true);
        when(companyRepository.existsById(3L)).thenReturn(false);


        // companyService = new CompanyServiceImpl(companyRepository);

    }

    @Test
    @DisplayName("Saving company name")
    public void createCompany() {
        // when
        Company savedCompany = companyService.saveCompany(company1);

        // then
        assertEquals(company1, savedCompany);
    }

    @Test 
    @DisplayName("Not saving company without a name")
    public void notCreateCompanyWithoutName(){
        Company company = new Company(); 
        Company notSavedCompany = companyService.saveCompany(company); 
        Company savedCompany = companyService.saveCompany(company1);

        // then
        assertNull(notSavedCompany);
        assertNotNull(savedCompany);
    }


    @Test
    public void getAllCompanies() {
        // when
        List<Company> retrievedCompanies = companyService.getAllCompanies();

        // then
        assertEquals(companies.size(), retrievedCompanies.size());
        assertTrue(retrievedCompanies.containsAll(companies));
    }

    // test correct get by id, should return company object
    @Test
    public void getCompanyById() {

        // when
        Company retrievedCompany = companyService.getCompany(1L);

        // then
        assertEquals(company1, retrievedCompany);
        assertEquals("Company 1", retrievedCompany.getName());
        assertEquals("Test description", retrievedCompany.getDescription());
        assertEquals("Test location", retrievedCompany.getLocation());
    }

    // test incorrect get by id, should throw ResourceNotFoundException
    @Test
    public void getFromNonExistentCompanyId_thenResourceNotFoundException() {
        // given non-existent company id
        Long nonExistingCompanyId = 3L;

        // then
        assertThrows(ResourceNotFoundException.class, () -> companyService.getCompany(3L), "Company not found with id " + nonExistingCompanyId);
    }

    // test update company with correct id, should return updated company object
    @Test
    public void updateCompanyByCompanyId() {
        // given
        Long companyId = 1L;

        Company updatedCompanyData = new Company();
        updatedCompanyData.setName("Updated Company Name");
        updatedCompanyData.setDescription("Updated description");
        updatedCompanyData.setLocation("Updated location");

        // when
        Company updatedCompany = companyService.updateCompany(companyId, updatedCompanyData);

        // then
        assertEquals("Updated Company Name", updatedCompany.getName());
        assertEquals("Updated description", updatedCompany.getDescription());
        assertEquals("Updated location", updatedCompany.getLocation());
    }

    // test update company with non-existent id, should throw
    // ResourceNotFoundException
    @Test
    public void updateFromNonExistentCompanyId_thenResourceNotFoundException() {
        // given
        Long companyId = 1L;

        Company updatedCompanyData = new Company();
        updatedCompanyData.setName("Updated Company Name");
        updatedCompanyData.setDescription("Updated description");

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class,
                () -> companyService.updateCompany(companyId, updatedCompanyData));
    }


    // test correct delete company, verifies that method is called once, throws exception when trying to delete non-existent company
    @Test
    public void deleteCompany() {
        // given
        Long existingCompanyId = 1L;


        // when
        companyService.deleteCompany(existingCompanyId);

        // check that the repository's deleteById method was called once
        verify(companyRepository, times(1)).deleteById(existingCompanyId);

    }

    @Test
    public void deleteNonExistentCompanyId_thenResourceNotFoundException() {
        // given
        Long nonExstingCompanyId = 3L;

        // then
        assertThrows(ResourceNotFoundException.class, () -> companyService.deleteCompany(nonExstingCompanyId));
    }

    // test get or create company, when company exists it returns it, when it
    // doesn't it creates it
    @Test
    public void getOrCreateCompany() {


        when(companyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(companyRepository.findByName("New Company")).thenReturn(Optional.empty());

        // when
        Company existingCompany = companyService.getOrCreateCompany("Company 1");
        Company newCompany = companyService.getOrCreateCompany("New company");

        // then
        assertEquals("Company 1", existingCompany.getName());
        assertEquals("New company", newCompany.getName());
    }

}