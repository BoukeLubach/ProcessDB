package com.wes.processdb.business;

import com.wes.processdb.data.entity.Process;
import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.repository.CompanyRepository;
import com.wes.processdb.data.repository.ProcessRepository;
import com.wes.processdb.web.requests.ProcessRequest;
import com.wes.processdb.business.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.*;

@AllArgsConstructor
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Override
    public Process createProcess(ProcessRequest processRequest) {

        Process processToSave = new Process();
        processToSave.setName(processRequest.getName());
        processToSave.setDescription(processRequest.getDescription());
        processToSave.setProduct(processRequest.getProduct());
        processToSave.setMaxProcessTemperature(processRequest.getMaxProcessTemperature());
        processToSave.setRawMaterial(processRequest.getRawMaterial());
        processToSave.setPower(processRequest.getPower());
        processToSave.setOperatingHours(processRequest.getOperatingHours());

        Long companyId = processRequest.getCompanyId();
        String companyName = processRequest.getCompanyName();

        if (companyId != null) {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
            processToSave.setCompany(company);
        } else if (companyName != null) {
            Company company = companyService.getOrCreateCompany(companyName);
            processToSave.setCompany(company);
        } else {
            throw new IllegalArgumentException("Either companyId or companyName must be provided");
        }

       
        Process processToBeReturned = processRepository.save(processToSave);

        return processToBeReturned;
    }

    // createa a process from processdata
    @Override
    public Process createProcess(Process process) {
        return processRepository.save(process);
    }

    // read all processes
    @Override
    public List<Process> getProcesses() {
        return (List<Process>) processRepository.findAll();
    }

    // read single process
    @Override
    public Process getProcess(Long processId) {
        Optional<Process> process = processRepository.findById(processId);
        return unwrapProcess(process, processId);
    }

    // update a process using a process object
    @Override
    public Process updateProcess(Long processId, Process process) {
        Optional<Process> optionalprocess = processRepository.findById(processId);
        Process processToUpdate = unwrapProcess(optionalprocess, processId);

        processToUpdate.setName(process.getName());
        processToUpdate.setDescription(process.getDescription());
        processToUpdate.setProduct(process.getProduct());
        processToUpdate.setMaxProcessTemperature(process.getMaxProcessTemperature());
        processToUpdate.setRawMaterial(process.getRawMaterial());
        processToUpdate.setPower(process.getPower());
        processToUpdate.setOperatingHours(process.getOperatingHours());

        return processRepository.save(processToUpdate);
    }

    // update a process using a processRequest object
    @Override
    public Process updateProcess(Long processId, ProcessRequest processRequest) {
        Optional<Process> process = processRepository.findById(processId);
        Process processToUpdate = unwrapProcess(process, processId);

        processToUpdate.setName(processRequest.getName());
        processToUpdate.setDescription(processRequest.getDescription());
        processToUpdate.setProduct(processRequest.getProduct());
        processToUpdate.setMaxProcessTemperature(processRequest.getMaxProcessTemperature());
        processToUpdate.setRawMaterial(processRequest.getRawMaterial());
        processToUpdate.setPower(processRequest.getPower());
        processToUpdate.setOperatingHours(processRequest.getOperatingHours());

        return processRepository.save(processToUpdate);
    }

    // delete a process
    @Override
    public void deleteProcess(Long processId) {
        if (processRepository.existsById(processId)) {
            processRepository.deleteById(processId);
        } else {
            throw new ResourceNotFoundException("Process not found for id: " + processId);
        }
    }

    // get a process by name, or create it if it does not exist
    @Override
    public Process getOrCreateProcess(String name) {
        Optional<Process> mightExistProcess = processRepository.findByName(name);

        // if process exists, return it
        if (!mightExistProcess.isEmpty()) {
            return mightExistProcess.get();
        }

        // otherwise, create it
        Process newProcess = new Process(name);
        return processRepository.save(newProcess);
    }

    // helper method to unwrap optional process
    static Process unwrapProcess(Optional<Process> process, Long processId) {
        // if process is not found, throw exception
        if (process.isEmpty()) {
            throw new ResourceNotFoundException("Process not found for id: " + processId);
        }

        // otherwise, return process
        return process.get();
    }

}
