package com.wes.processdb.web;

import com.wes.processdb.business.UtilityService;
import com.wes.processdb.data.entity.Utility;
import com.wes.processdb.data.entity.utilities.Electricity;
import com.wes.processdb.data.entity.utilities.NaturalGas;
import com.wes.processdb.data.entity.utilities.Water;
import com.wes.processdb.data.entity.utilities.NonCombustionGas;
import com.wes.processdb.web.requests.ElectricityRequest;
import com.wes.processdb.web.requests.NaturalGasRequest;
import com.wes.processdb.web.requests.WaterRequest;
import com.wes.processdb.web.requests.NonCombustionGasRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wes.processdb.business.exception.ValidationException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UtilityRestController {

    private final UtilityService utilityService;

    public UtilityRestController(UtilityService utilityService) {
        this.utilityService = utilityService;

    }

    // post new utility
    @PostMapping("/utility")
    public ResponseEntity<Object> createUtility(@RequestBody Object request) {
        String category = ((Map<String, String>) request).get("category");
        if ("electricity".equals(category)) {
            ElectricityRequest electricityRequest = new ObjectMapper().convertValue(request, ElectricityRequest.class);
            Electricity savedElectricity = utilityService.createElectricityFromRequest(electricityRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedElectricity);
        } else if ("naturalgas".equals(category)) {
            NaturalGasRequest naturalGasRequest = new ObjectMapper().convertValue(request, NaturalGasRequest.class);
            NaturalGas savedNaturalGas = utilityService.createNaturalGasFromRequest(naturalGasRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNaturalGas);

        } else if ("water".equals(category)) {

            WaterRequest waterRequest = new ObjectMapper().convertValue(request, WaterRequest.class);
            Water savedWater = utilityService.createWaterFromRequest(waterRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWater);

        } else if ("NonCombustionGas".equals(category)) {
            NonCombustionGasRequest nonCombustionGasRequest = new ObjectMapper().convertValue(request,
                    NonCombustionGasRequest.class);

            NonCombustionGas savedNonCombustionGas = utilityService
                    .createNonCombustionGasFromRequest(nonCombustionGasRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNonCombustionGas);

        } else {
            throw new IllegalArgumentException("Invalid category: " + category);
        }

    }

    // get list by category
    @GetMapping("/utility/category/{category}")
    public ResponseEntity<List<? extends Utility>> getAllUtilitiesByCategory(@PathVariable String category) {
        if ("electricity".equals(category)) {
            List<Electricity> electricityList = utilityService.getAllElectricity();
            return new ResponseEntity<>(electricityList, HttpStatus.OK);
        } else if ("naturalgas".equals(category)) {
            List<NaturalGas> naturalGasList = utilityService.getAllNaturalGas();
            return new ResponseEntity<>(naturalGasList, HttpStatus.OK);
        } else {
            // Handle unknown category
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // get list of utilities, optionally filtered by process ID
    @GetMapping("/utility")
    public ResponseEntity<List<? extends Utility>> getUtilities(
            @RequestParam(value = "processid", required = false) Long processId) {
        if (processId != null) {
            // Retrieve utilities based on the provided process ID and return the filtered
            // utilities
            List<? extends Utility> filteredUtilities = utilityService.getUtilitiesByProcessId(processId);
            return new ResponseEntity<>(filteredUtilities, HttpStatus.OK);
        } else {
            // Retrieve all utilities and return the list
            List<Utility> allUtilities = utilityService.getAllUtilities();
            return new ResponseEntity<>(allUtilities, HttpStatus.OK);
        }
    }

    // get individual utility
    @GetMapping("/utility/{id}")
    public ResponseEntity<Utility> getUtilityById(@PathVariable Long id) {
        Utility utility = utilityService.getUtility(id);
        return new ResponseEntity<>(utility, HttpStatus.OK);
    }

    // update utility
    // to be implemented

    // delete utility, return 204 if successful
    @DeleteMapping("/utility/{id}")
    public ResponseEntity<?> deleteUtility(@PathVariable Long id) {
        utilityService.deleteUtility(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/process/{id}/utility")
    public ResponseEntity<Object> addUtilityToProcess(@PathVariable Long id, @RequestBody Object request) {

        Object categoryObj = ((Map<String, Object>) request).get("category");

        if (!(categoryObj instanceof String)) {
            throw new ValidationException("Category must be a string");
        }

        String category = (String) categoryObj;
        Set<String> validCategories = new HashSet<>(
                Arrays.asList("electricity", "naturalgas", "water", "NonCombustionGas"));

        if (!validCategories.contains(category)) {
            throw new ValidationException("Invalid category: " + category);
        }

        Utility utility = utilityService.createUtilityFromRequest(category, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(utility);
    }



}
