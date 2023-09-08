package com.wes.processdb;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.wes.processdb.business.exception.DuplicateFieldException;
import com.wes.processdb.business.exception.ResourceNotFoundException;
import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.repository.CompanyRepository;

import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
// @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:testData.sql" }, config = @SqlConfig(encoding = "utf-8"))
public class CompanyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CompanyRepository companyRepository;

    // @BeforeEach
    // void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
    //     JdbcTestUtils.deleteFromTables(jdbcTemplate, "company");
    // }

    @Test
    public void testCreateCompany() throws Exception {
        Company company = new Company("testcompanyname", "description", "location");
        String companyJson = objectMapper.writeValueAsString(company);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(companyJson, headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyJson))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Company createdCompany = companyRepository.findByName("testcompanyname").orElse(null);
        assertThat(createdCompany).isNotNull();
        assertEquals(createdCompany.getLocation(), "location");
    }

    @Test
    public void testCreateCompanyWithMissingName() throws Exception {
        Company company = new Company(null, "description", "location");
        String companyJson = objectMapper.writeValueAsString(company);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(companyJson, headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyJson))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void testCreateCompanyWithExistingName() throws Exception {
        Company company = new Company("Acme Chemicals", "description", "location");
        String companyJson = objectMapper.writeValueAsString(company);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(companyJson, headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyJson))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // check assertion was thrown
        assertEquals(mvcResult.getResolvedException().getClass(), DuplicateFieldException.class);
    }

    @Test
    public void testGetCompany() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/company/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertNotNull(responseBody);

        // get name field from response body
        ObjectMapper mapper = new ObjectMapper();
        Company responseObj = mapper.readValue(responseBody, Company.class);
        String retrievedName = responseObj.getName();

        assertEquals("Acme Chemicals", retrievedName);
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void testGetCompanyNotFound() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/company/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testUpdateCompany() throws Exception {
        Company company = new Company("testcompanyname", "description", "location");
        String companyJson = objectMapper.writeValueAsString(company);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(companyJson, headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/company/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyJson))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        Company updatedCompany = companyRepository.findById(1L).orElse(null);
        assertThat(updatedCompany).isNotNull();
        assertEquals(updatedCompany.getLocation(), "location");
    }

    @Test
    public void testUpdateCompanyNotFound() throws Exception {
        Company company = new Company("testcompanyname", "description", "location");
        String companyJson = objectMapper.writeValueAsString(company);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(companyJson, headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/company/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyJson))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertEquals(mvcResult.getResolvedException().getClass(), ResourceNotFoundException.class);
    }

    @Test
    public void testDeleteCompany() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/company/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Company deletedCompany = companyRepository.findById(1L).orElse(null);
        assertThat(deletedCompany).isNull();
    }

    @Test
    public void testDeleteCompanyNotFound() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/company/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertEquals(mvcResult.getResolvedException().getClass(), ResourceNotFoundException.class);
    }

}
