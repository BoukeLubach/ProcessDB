package com.wes.processdb;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.wes.processdb.business.ProcessServiceImpl;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.wes.processdb.business.CompanyService;
import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.entity.Process;
import com.wes.processdb.data.repository.CompanyRepository;
import com.wes.processdb.data.repository.ProcessRepository;
import com.wes.processdb.web.requests.ProcessRequest;
import com.wes.processdb.business.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.ArgumentMatchers;

// @ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProcessServiceTests {

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private ProcessServiceImpl processService;

    Company company1;
    Process process1;
    Process process2;
    Process process3;

    List<Process> processes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        company1 = new Company("Test Company 1", "Test Description 1", "Test Location 1");
        process1 = new Process("Test Process 1", "Test Description 1", "Test product 1", 100.0, "coal", 500.0, 10,
                company1);
        process2 = new Process("Test Process 2", "Test Description 2", "Test product 2", 100.0, "coal", 500.0, 10,
                company1);
        process3 = new Process("Test Process 3", "Test Description 3", "Test product 3", 100.0, "coal", 500.0, 10,
                company1);
        processes = Arrays.asList(process1, process2, process3);

    }

    @Test
    public void createFromProcessObject() {

        // Arrange
        when(processRepository.findById(1L)).thenReturn(Optional.of(process1));
        when(processRepository.findById(2L)).thenReturn(Optional.of(process2));
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Process savedProcess = processService.createProcess(process1);

        // // Assert
        assertNotNull(savedProcess);
        assertEquals("Test Process 1", savedProcess.getName());
        assertEquals("Test Description 1", savedProcess.getDescription());

        assertEquals("Test product 1", savedProcess.getProduct());
;
        assertEquals(100.0, savedProcess.getMaxProcessTemperature());
        
        assertEquals(500.0, savedProcess.getPower());
        assertEquals(10, savedProcess.getOperatingHours());
        assertEquals(company1, savedProcess.getCompany());

        Process savedProcess2 = processService.createProcess(process2);

        // // Assert
        assertNotNull(savedProcess2);
        assertEquals("Test Process 2", savedProcess2.getName());
        assertEquals("Test Description 2", savedProcess2.getDescription());
        assertEquals(company1, savedProcess2.getCompany());

        verify(processRepository, times(1)).save(process1);
        verify(processRepository, times(1)).save(process2);

    }

    @Test
    public void createProcessFromProcessRequestWithCompanyName(){
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(companyService.getOrCreateCompany("Company 1")).thenReturn(company1);



        ProcessRequest processRequest = new ProcessRequest("Test Process 1", "Test Description 1", "Company 1", "Test product 1", 50.0,
                30.0, 500.0, 8000);

       Process newProcess = processService.createProcess(processRequest); 
           
       verify(processRepository, times(1)).save(any());
       verify(companyService, times(1)).getOrCreateCompany("Company 1");

       assertEquals("Test Process 1", newProcess.getName());
        assertEquals("Test Description 1", newProcess.getDescription());
        assertEquals("Test product 1", newProcess.getProduct());
        
    }

    @Test
    public void createProcessFromProcessRequestWithCompanyId(){
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company1));

        ProcessRequest processRequest = new ProcessRequest("Test Process 1", "Test Description 1", 1L, "Test product 1",  50.0,
                30.0, 500.0, 8000);

       Process newProcess = processService.createProcess(processRequest); 
       verify(processRepository, times(1)).save(any());
       verify(companyRepository, times(1)).findById(1L); 

       assertEquals("Test Process 1", newProcess.getName());
        assertEquals("Test Description 1", newProcess.getDescription());

        assertEquals("Test product 1", newProcess.getProduct());
    }

    @Test
    public void createProcessFromProcessRequestWithNonExistingCompanyName() {

        Company newCompany = new Company("New Company");
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(companyService.getOrCreateCompany("New Company")).thenReturn(newCompany);

        ProcessRequest processRequest = new ProcessRequest("Test Process 1", "Test Description 1", "New Company",
                "Test product 1", 50.0,
                30.0, 500.0, 8000);

        Process newProcess = processService.createProcess(processRequest);

        verify(processRepository, times(1)).save(any());
        verify(companyService, times(1)).getOrCreateCompany("New Company");

        assertEquals("Test Process 1", newProcess.getName());
        assertEquals("Test Description 1", newProcess.getDescription());
        assertEquals("Test product 1", newProcess.getProduct());
    }

    @Test
    public void attemptCreateProcessFromProcessRequestWithNonExistingCompanyId() {

        doThrow(new ResourceNotFoundException("Company not found for id: " + 2L)).when(companyRepository).findById(2L);

        ProcessRequest processRequest = new ProcessRequest("Test Process 1", "Test Description 1", 1L, "Test product 1",
                50.0, 30.0, 500.0, 8000);
        assertThrows(ResourceNotFoundException.class, () -> processService.createProcess(processRequest));

        verify(companyRepository, times(1)).findById(1L);

    }

    @Test
    public void getAllProcesses() {
        // Arrange
        when(processRepository.findAll()).thenReturn(processes);

        // Act  
        List<Process> retrievedProcesses = processService.getProcesses();
        // Assert
        assertThat(retrievedProcesses).containsExactly(process1, process2, process3);
        verify(processRepository, times(1)).findAll();
    }

    @Test
    public void getProcessById() {
        // 
        when(processRepository.findById(1L)).thenReturn(Optional.of(process1));
        when(processRepository.findById(2L)).thenReturn(Optional.of(process2));
        when(processRepository.findById(3L)).thenReturn(Optional.of(process3));

        // Act
        Process foundProcess1 = processService.getProcess(1L);
        Process foundProcess2 = processService.getProcess(2L);
        Process foundProcess3 = processService.getProcess(3L);

        // Assert
        assertNotNull(foundProcess1);
        assertNotNull(foundProcess2);
        assertNotNull(foundProcess3);

        assertEquals(foundProcess1, process1);
        assertEquals(foundProcess2, process2);
        assertEquals(foundProcess3, process3);

        verify(processRepository, times(1)).findById(1L);
    }

    @Test
    public void getFromNonExistentProcessIdThenResourceNotFoundException() {
        // arrange
        Long nonExistentId = 4L;
        when(processRepository.findById(4L)).thenReturn(Optional.empty());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> processService.getProcess(nonExistentId));
        verify(processRepository, times(1)).findById(4L);
    }

    @Test
    public void updateFromProcessObject() {

        // Arrange
        Process processToUpdate = new Process("Updated Test Process", "Updated Test Process Description",
                "Updated Test product",  110.0, "coal", 600.0, 8060, company1);

        when(processRepository.findById(1L)).thenReturn(Optional.of(process1));
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Process updatedProcess = processService.updateProcess(1L, processToUpdate);

        // Assert
        assertNotNull(updatedProcess);
        assertEquals("Updated Test Process", updatedProcess.getName());
        assertEquals("Updated Test Process Description", updatedProcess.getDescription());
        assertEquals("Updated Test product", updatedProcess.getProduct());
        assertEquals(110.0, updatedProcess.getMaxProcessTemperature());
        assertEquals("coal", updatedProcess.getRawMaterial());
        assertEquals(600.0, updatedProcess.getPower());
        assertEquals(8060, updatedProcess.getOperatingHours());

        verify(processRepository, times(1)).findById(1L);
        verify(processRepository, times(1)).save(process1);
    }

    @Test
    void updateFromProcessRequest() {
        // Given
        Long processId = 1L;
        ProcessRequest processRequest = new ProcessRequest();
        processRequest.setName("Updated Process Name");
        processRequest.setDescription("Updated Process Description");
        processRequest.setProduct("Updated product"); 
        processRequest.setMaxProcessTemperature(100.0);
        processRequest.setExitProcessTemperature(80.0);
        processRequest.setPower(500.0);
        processRequest.setOperatingHours(8);
        processRequest.setRawMaterial("coal");
        when(processRepository.findById(processId)).thenReturn(Optional.of(process1));
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Process updatedProcess = processService.updateProcess(processId, processRequest);

        // Then
        assertEquals(processRequest.getName(), updatedProcess.getName());
        assertEquals(processRequest.getDescription(), updatedProcess.getDescription());
        assertEquals(processRequest.getProduct(), updatedProcess.getProduct());
        assertEquals(processRequest.getMaxProcessTemperature(), updatedProcess.getMaxProcessTemperature());
        assertEquals(processRequest.getRawMaterial(), updatedProcess.getRawMaterial());
        assertEquals(processRequest.getPower(), updatedProcess.getPower());
        assertEquals(processRequest.getOperatingHours(), updatedProcess.getOperatingHours());
    }

    // test update company with non-existent id, throws exception
    @Test
    public void updateNonExistingProcessIdThenResourceNotFoundException() {
        // given
        Long processId = 4L;
        when(processRepository.findById(4L)).thenReturn(Optional.empty());
        when(processRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Process updateProcess = new Process();
        updateProcess.setName("Updated process Name");

        // then
        assertThrows(ResourceNotFoundException.class, () -> processService.updateProcess(processId, updateProcess));
    }

    @Test
    public void deleteProcess() {
        // arrange
        Long existingProcessId = 1L;

        when(processRepository.existsById(1L)).thenReturn(true);
        when(processRepository.existsById(2L)).thenReturn(true);
        when(processRepository.existsById(3L)).thenReturn(true);
        when(processRepository.existsById(4L)).thenReturn(false);

        // act
        processService.deleteProcess(existingProcessId);

        // check that the repository's deleteById method was called once
        verify(processRepository, times(1)).deleteById(existingProcessId);

    }

    @Test
    public void deleteNonExistentProcessIdThenResourceNotFoundException() {
        // given
        Long nonExstingProcessId = 4L;

        when(processRepository.existsById(1L)).thenReturn(true);
        when(processRepository.existsById(2L)).thenReturn(true);
        when(processRepository.existsById(3L)).thenReturn(true);
        when(processRepository.existsById(4L)).thenReturn(false);

        // check exception is thrown when trying to delete a non-existent company
        assertThrows(ResourceNotFoundException.class, () -> processService.deleteProcess(nonExstingProcessId));
    }

}