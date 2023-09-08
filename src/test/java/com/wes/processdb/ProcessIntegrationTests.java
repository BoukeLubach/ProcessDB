package com.wes.processdb;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.wes.processdb.data.entity.Process;
import com.wes.processdb.data.repository.CompanyRepository;
import com.wes.processdb.data.repository.ProcessRepository;

import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

// @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:testData.sql" }, config = @SqlConfig(encoding = "utf-8"))
public class ProcessIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ProcessRepository processRepository;

    @Autowired
    CompanyRepository companyRepository;


    @Test
    public void testCreateProcess() throws Exception {
        // Create a Company for the Process
        Company company = new Company("testcompanyname", "description", "location");
        companyRepository.save(company);

        // Create a Process object
        Process process = new Process("Random proces sdfjslfjadsf");
        process.setCompany(company);

        // Convert Process object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String processJson = objectMapper.writeValueAsString(process);

        // Set request headers and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(processJson, headers);

        // Perform POST request to create the Process
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andReturn();

        // Assertions
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Process createdProcess = processRepository.findByName("Random proces sdfjslfjadsf").orElse(null);
        assertThat(createdProcess).isNotNull();
        assertEquals("testcompanyname", createdProcess.getCompany().getName());
    }

    @Test
    public void testGetProcess() throws Exception {
        // Perform GET request to retrieve the Process
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/process/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assertions
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertNotNull(responseBody);

        ObjectMapper mapper = new ObjectMapper();
        Process retrievedProcess = mapper.readValue(responseBody, Process.class);
        String retrievedName = retrievedProcess.getName();
        assertEquals("Specialty Chemical Synthesis", retrievedName);
    }

    @Test
    public void testGetProcessNotFound() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/process/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testUpdateProcess() throws Exception {
        // Fetch an existing Process object
        Process existingProcess = processRepository.findById(1L).orElse(null);
        assertNotNull(existingProcess);

        // Update the properties of the Process object
        existingProcess.setDescription("Updated Description");
        existingProcess.setProduct("Updated product");

        // Convert updated Process object to JSON
        String updatedProcessJson = objectMapper.writeValueAsString(existingProcess);

        // Set request headers and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(updatedProcessJson, headers);

        // Perform PUT request to update the Process
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/process/" + existingProcess.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedProcessJson))
                .andReturn();

        // Assertions
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Process updatedProcess = processRepository.findById(existingProcess.getId()).orElse(null);
        assertNotNull(updatedProcess);
        assertEquals("Updated Description", updatedProcess.getDescription());
        assertEquals("Updated product", updatedProcess.getProduct());

    }

    @Test
    public void testUpdateProcessNotFound() throws Exception {
        // Create a Process object
        Process process = new Process("Non-Existing Process");

        // Convert Process object to JSON
        String processJson = objectMapper.writeValueAsString(process);

        // Set request headers and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(processJson, headers);

        // Perform PUT request to update a non-existing Process
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/process/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andReturn();

        // Assertions
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());
    }

    @Test
    public void testDeleteProcess() throws Exception {
        // Perform DELETE request to delete an existing Process
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/process/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
        Process deletedProcess = processRepository.findById(1L).orElse(null);
        assertNull(deletedProcess);
    }

    @Test
    public void testDeleteProcessNotFound() throws Exception {
        // Perform DELETE request to delete a non-existing Process
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/process/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

}
