package com.wes.processdb.business;

import lombok.*;
import org.springframework.stereotype.Service;

import com.wes.processdb.data.entity.utilities.Electricity;
import com.wes.processdb.data.entity.utilities.NaturalGas;
import com.wes.processdb.data.entity.utilities.Water;
import com.wes.processdb.data.entity.utilities.NonCombustionGas;
import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.entity.Process;
import com.wes.processdb.data.entity.Utility;
import com.wes.processdb.data.repository.CompanyRepository;
import com.wes.processdb.data.repository.ProcessRepository;
import com.wes.processdb.data.repository.UtilityRepository;

import com.wes.processdb.web.requests.ElectricityRequest;
import com.wes.processdb.web.requests.NaturalGasRequest;
import com.wes.processdb.web.requests.WaterRequest;
import com.wes.processdb.web.requests.NonCombustionGasRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wes.processdb.business.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@Service
public class UtilityServiceImpl implements UtilityService {

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessService processService;

    // create the different utilities
    @Override
    public Utility saveUtility(Utility utility) {

        return utilityRepository.save(utility);
    }

    @Override
    public Electricity createElectricityFromRequest(ElectricityRequest electricityRequest) {

        Electricity electricity = new Electricity();

        // set common fields
        electricity.setName(electricityRequest.getName());
        electricity.setAmount(electricityRequest.getAmount());
        electricity.setUnitOfMeasurement(electricityRequest.getUnitOfMeasurement());
        electricity.setPurchased(electricityRequest.getPurchased());
        electricity.setCategory("electricity");
        electricity.setFrequency(electricityRequest.getFrequency());
        electricity.setAC_or_DC(electricityRequest.getAC_or_DC());

        // set source based on source id
        if (electricityRequest.getSourceId() != null) {
            electricity.setSource(unwrapUtility((utilityRepository.findById(electricityRequest.getSourceId())),
                    electricityRequest.getSourceId()));
        }

        // set process based on process id or process name (priority given to process
        // id)
        if (electricityRequest.getProcessId() != null) {
            Process process = unwrapProcess(processRepository.findById(electricityRequest.getProcessId()),
                    electricityRequest.getProcessId());
            electricity.setProcess(process);
        } else if (electricityRequest.getProcessName() != null) {
            Process process = processService.getOrCreateProcess(electricityRequest.getProcessName());
            electricity.setProcess(process);
        } else {
            throw new IllegalArgumentException("Either processId or processName must be provided");
        }

        // set electricity-specific fields
        electricity.setVoltage(electricityRequest.getVoltage());
        return saveElectricity(electricity);
    }

    @Override
    public Electricity saveElectricity(Electricity electricity) {
        return utilityRepository.save(electricity);
    }

    @Override
    public NaturalGas createNaturalGasFromRequest(NaturalGasRequest naturalGasRequest) {
        NaturalGas naturalGas = new NaturalGas();

        // set common fields
        naturalGas.setName(naturalGasRequest.getName());
        naturalGas.setAmount(naturalGasRequest.getAmount());
        naturalGas.setUnitOfMeasurement(naturalGasRequest.getUnitOfMeasurement());
        naturalGas.setPurchased(naturalGasRequest.getPurchased());
        naturalGas.setCategory("naturalGas");

        // set source based on source id
        if (naturalGasRequest.getSourceId() != null) {
            naturalGas.setSource(unwrapUtility((utilityRepository.findById(naturalGasRequest.getSourceId())),
                    naturalGasRequest.getSourceId()));
        }

        // set process based on process id or process name (priority given to process
        // id)
        if (naturalGasRequest.getProcessId() != null) {
            // get process from process repository, if it exists, otherwise throw exception
            Process process = unwrapProcess(processRepository.findById(naturalGasRequest.getProcessId()),
                    naturalGasRequest.getProcessId());
            naturalGas.setProcess(process);
        } else if (naturalGasRequest.getProcessName() != null) {
            Process process = processService.getOrCreateProcess(naturalGasRequest.getProcessName());
            naturalGas.setProcess(process);
        } else {
            throw new IllegalArgumentException("Either processId or processName must be provided");
        }

        // set natural gas-specific fields
        naturalGas.setGasType(naturalGasRequest.getGasType());
        naturalGas.setLHV(naturalGasRequest.getLHV());
        naturalGas.setHHV(naturalGasRequest.getHHV());

        return saveNaturalGas(naturalGas);
    }

    @Override
    public NaturalGas saveNaturalGas(NaturalGas naturalGas) {

        return utilityRepository.save(naturalGas);
    }

    @Override
    public Water createWaterFromRequest(WaterRequest waterRequest) {
        Water water = new Water();
        // set common fields
        water.setName(waterRequest.getName());
        water.setAmount(waterRequest.getAmount());
        water.setUnitOfMeasurement(waterRequest.getUnitOfMeasurement());
        water.setPurchased(waterRequest.getPurchased());
        water.setCategory("water");

        // set source based on source id
        if (waterRequest.getSourceId() != null) {
            water.setSource(unwrapUtility((utilityRepository.findById(waterRequest.getSourceId())),
                    waterRequest.getSourceId()));
        }

        // set process based on process id or process name (priority given to process
        // id)
        if (waterRequest.getProcessId() != null) {
            Process process = unwrapProcess(processRepository.findById(waterRequest.getProcessId()),
                    waterRequest.getProcessId());
            water.setProcess(process);
        } else if (waterRequest.getProcessName() != null) {
            Process process = processService.getOrCreateProcess(waterRequest.getProcessName());
            water.setProcess(process);
        } else {
            throw new IllegalArgumentException("Either processId or processName must be provided");
        }

        // set water-specific fields
        water.setTemperature(waterRequest.getTemperature());
        water.setQuality(waterRequest.getQuality());
        water.setPurpose(waterRequest.getPurpose());

        return saveWater(water);
    }

    @Override
    public Water saveWater(Water water) {

        return utilityRepository.save(water);
    }

    @Override
    public NonCombustionGas createNonCombustionGasFromRequest(NonCombustionGasRequest nonCombustionGasRequest) {
        NonCombustionGas nonCombustionGas = new NonCombustionGas();
        // set common fields
        nonCombustionGas.setName(nonCombustionGasRequest.getName());
        nonCombustionGas.setAmount(nonCombustionGasRequest.getAmount());
        nonCombustionGas.setUnitOfMeasurement(nonCombustionGasRequest.getUnitOfMeasurement());
        nonCombustionGas.setPurchased(nonCombustionGasRequest.getPurchased());
        nonCombustionGas.setCategory("nonCombustionGas");

        // set source based on source id
        if (nonCombustionGasRequest.getSourceId() != null) {
            nonCombustionGas
                    .setSource(unwrapUtility((utilityRepository.findById(nonCombustionGasRequest.getSourceId())),
                            nonCombustionGasRequest.getSourceId()));
        }

        // set process based on process id or process name (priority given to process
        // id)
        if (nonCombustionGasRequest.getProcessId() != null) {
            Process process = unwrapProcess(processRepository.findById(nonCombustionGasRequest.getProcessId()),
                    nonCombustionGasRequest.getProcessId());
            nonCombustionGas.setProcess(process);
        } else if (nonCombustionGasRequest.getProcessName() != null) {
            Process process = processService.getOrCreateProcess(nonCombustionGasRequest.getProcessName());
            nonCombustionGas.setProcess(process);
        } else {
            throw new IllegalArgumentException("Either processId or processName must be provided");
        }

        // set nonCombustionGas-specific fields
        nonCombustionGas.setGasType(nonCombustionGasRequest.getGasType());
        nonCombustionGas.setPressure(nonCombustionGasRequest.getPressure());

        return saveNonCombustionGas(nonCombustionGas);
    }

    @Override
    public NonCombustionGas saveNonCombustionGas(NonCombustionGas nonCombustionGas) {

        return utilityRepository.save(nonCombustionGas);
    }

    // end of create the different utilities

    // get individual utility
    @Override
    public Utility getUtility(Long utilityId) {
        Optional<Utility> utility = utilityRepository.findById(utilityId);
        return unwrapUtility(utility, utilityId);
    }
    // end of get individual utility

    // get list of different utilities
    @Override
    public List<Electricity> getAllElectricity() {
        return (List<Electricity>) utilityRepository.findAllElectricity();
        // return (List<Electricity>) utilityRepository.findAllElectricity();
    }

    @Override
    public List<NaturalGas> getAllNaturalGas() {
        return (List<NaturalGas>) utilityRepository.findAllNaturalGas();
        // return (List<NaturalGas>) utilityRepository.findAllNaturalGas();
    }

    @Override
    public List<Water> getAllWater() {
        return (List<Water>) utilityRepository.findAllWater();
        // return (List<Water>) utilityRepository.findAllWater();
    }

    @Override
    public List<NonCombustionGas> getAllNonCombustionGas() {
        return (List<NonCombustionGas>) utilityRepository.findAllNonCombustionGas();
        // return (List<NonCombustionGas>) utilityRepository.findAllNonCombustionGas();
    }

    @Override
    public List<Utility> getUtilitiesByProcessId(Long processId) {
        return (List<Utility>) utilityRepository.findUtilitiesByProcessId();
    }

    @Override
    public List<Utility> getAllUtilities() {
        // return (List<Utility>) utilityRepository.findAll(Sort.by("utilityId"));
        return (List<Utility>) utilityRepository.findAll();
    }
    // end of get list of different utilities

    // delete the different utilities
    @Override
    public void deleteUtility(Long id) {
        utilityRepository.deleteById(id);
    }
    // end of delete the different utilities

    

    // create utility from request
    public Utility createUtilityFromRequest(String category, Object request) {
        if ("electricity".equals(category)) {
            ElectricityRequest electricityRequest = new ObjectMapper().convertValue(request, ElectricityRequest.class);
            return createElectricityFromRequest(electricityRequest);
        } else if ("naturalgas".equals(category)) {
            NaturalGasRequest naturalGasRequest = new ObjectMapper().convertValue(request, NaturalGasRequest.class);
            return createNaturalGasFromRequest(naturalGasRequest);
        } else if ("water".equals(category)) {
            WaterRequest waterRequest = new ObjectMapper().convertValue(request, WaterRequest.class);
            return createWaterFromRequest(waterRequest);
        } else if ("NonCombustionGas".equals(category)) {
            NonCombustionGasRequest nonCombustionGasRequest = new ObjectMapper().convertValue(request,
                    NonCombustionGasRequest.class);
            return createNonCombustionGasFromRequest(nonCombustionGasRequest);
        } else {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }



    


    // unwrap process
    static Utility unwrapUtility(Optional<Utility> utility, Long utilityId) {
        if (utility.isEmpty()) {
            throw new ResourceNotFoundException("Utility not found with id: " + utilityId);
        }
        return utility.get();
    }

    static Company unwrapCompany(Optional<Company> company, Long companyId) {
        if (company.isEmpty()) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId);
        }
        return company.get();
    }

    static Process unwrapProcess(Optional<Process> process, Long processId) {
        if (process.isEmpty()) {
            throw new ResourceNotFoundException("Process not found with id: " + processId);
        }
        return process.get();
    }

}
