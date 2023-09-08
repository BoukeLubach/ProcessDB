package com.wes.processdb.web;

import com.wes.processdb.business.CompanyService;
import com.wes.processdb.data.entity.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import com.wes.processdb.business.exception.DuplicateFieldException;
import com.wes.processdb.business.exception.ValidationException;
import com.wes.processdb.business.exception.InternalServerException;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.stream.Collectors;

import java.util.List;
import javax.validation.Valid;

@RestController
public class CompanyRestController {

    private final CompanyService companyService;

    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company")
    public ResponseEntity<Object> createCompany(@Valid @RequestBody Company company, BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        if (!errors.isEmpty()) {
            String errorMessage = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessage);
        }

        companyService.saveCompany(company);
        return new ResponseEntity<>(company, HttpStatus.CREATED);

    }

    @GetMapping("/company")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        Company companyToReturn = companyService.getCompany(companyId);

        return new ResponseEntity<>(companyToReturn, HttpStatus.OK);
    }

    

    @PutMapping("/company/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long companyId, @Valid @RequestBody Company company, BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        if (!errors.isEmpty()) {
            String errorMessage = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessage);
        }
        return new ResponseEntity<>(companyService.updateCompany(companyId, company), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {

        companyService.deleteCompany(companyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
