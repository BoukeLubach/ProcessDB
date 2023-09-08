package com.wes.processdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wes.processdb.business.UtilityServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.wes.processdb.data.entity.Company;
import com.wes.processdb.data.entity.Process;
import com.wes.processdb.data.entity.Utility;
import com.wes.processdb.data.entity.utilities.Electricity;
import com.wes.processdb.data.entity.utilities.NaturalGas;
import com.wes.processdb.data.repository.CompanyRepository;
import com.wes.processdb.data.repository.ProcessRepository;
import com.wes.processdb.data.repository.UtilityRepository;
import com.wes.processdb.web.requests.ElectricityRequest;
import com.wes.processdb.web.requests.NaturalGasRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
public class UtilityServiceTests {

    @Mock
    private UtilityRepository utilityRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ProcessRepository processRepository;

    @InjectMocks
    private UtilityServiceImpl utilityService;

    private Process process1;
    private Process process2;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        process1 = new Process();
        process1.setName("Process 1");
        process1.setId(1L);

        process2 = new Process();
        process2.setName("Process 2");
        process2.setId(2L);

    }

    @Test
    public void testSaveElectricity() {
        // create new electricity object
        Electricity electricity = new Electricity();
        electricity.setName("Electricity 1");

        electricity.setUnitOfMeasurement("kWh");
        electricity.setPurchased(true);

        electricity.setVoltage(230.0);
        electricity.setId(1L);
        electricity.setProcess(process1);

        // mock the save method
        when(utilityRepository.save(electricity)).thenReturn(electricity);

        // save the electricity object
        Electricity savedElectricity = utilityService.saveElectricity(electricity);

        // assert that the electricity object is saved
        Assertions.assertEquals(electricity, savedElectricity);

    }

    @Test
    public void testSaveNaturalGas() {
        // create new natural gas object
        NaturalGas naturalGas = new NaturalGas();
        naturalGas.setName("Natural Gas 1");

        naturalGas.setUnitOfMeasurement("m3");
        naturalGas.setPurchased(false);
        naturalGas.setPressure(100.0);
        naturalGas.setGasType("Natural gas");
        naturalGas.setLHV(50.0);
        naturalGas.setHHV(55.0);
        naturalGas.setId(2L);
        naturalGas.setProcess(process2);

        // mock the save method
        when(utilityRepository.save(naturalGas)).thenReturn(naturalGas);

        // save the natural gas object
        NaturalGas savedNaturalGas = utilityService.saveNaturalGas(naturalGas);

        // assert that the natural gas object is saved
        Assertions.assertEquals(naturalGas, savedNaturalGas);
    }

    @Test
    public void testGetAllUtilities() {
        // create a list of utilities
        List<Utility> utilities = new ArrayList<>();
        utilities.add(new Electricity("Electricity 1", 230.0));
        utilities.add(new NaturalGas("Natural Gas 1", 100.0));

        // mock the findAll method
        when(utilityRepository.findAll()).thenReturn(utilities);

        // store the list of utilities
        List<Utility> allUtilities = utilityService.getAllUtilities();

        // assert correct number of utilities are returned
        assertEquals(2, utilities.size());
        assertEquals(2, allUtilities.size());

        // assert that the first utility in the list is an Electricity object with the
        // expected values
        assertTrue(allUtilities.get(0) instanceof Electricity);
        assertEquals("Electricity 1", allUtilities.get(0).getName());
        assertEquals(230.0, ((Electricity) allUtilities.get(0)).getVoltage());

        // assert that the second utility in the list is a NaturalGas object with the
        // expected values
        assertTrue(allUtilities.get(1) instanceof NaturalGas);
        assertEquals("Natural Gas 1", allUtilities.get(1).getName());
        assertEquals(100.0, ((NaturalGas) allUtilities.get(1)).getPressure());
    }

    @Test
    public void testGetAllElectricity() {
        // create a list of electricity objects
        List<Electricity> electricityList = new ArrayList<>();
        electricityList.add(new Electricity("Electricity 1", 230.0));
        electricityList.add(new Electricity("Electricity 2", 230.0));

        // mock the findAll method
        when(utilityRepository.findAllElectricity()).thenReturn(electricityList);

        // store the list of electricity objects
        List<Electricity> allElectricity = utilityService.getAllElectricity();

        // assert correct number of electricity objects are returned
        assertEquals(2, electricityList.size());
        assertEquals(2, allElectricity.size());

        // assert that the first electricity object in the list has the expected values
        assertEquals("Electricity 1", allElectricity.get(0).getName());
        assertEquals(230.0, allElectricity.get(0).getVoltage());

        // assert that the second electricity object in the list has the expected values
        assertEquals("Electricity 2", allElectricity.get(1).getName());
        assertEquals(230.0, allElectricity.get(1).getVoltage());
    }

    // test get all naturalgas
    @Test
    public void testGetAllNaturalgas() {
        // create a list of naturalgas objects
        List<NaturalGas> naturalGasList = new ArrayList<>();
        NaturalGas natgas1 = new NaturalGas("Natural Gas 1", 100.0);
        NaturalGas natgas2 = new NaturalGas("Natural Gas 2", 100.0);

        naturalGasList.add(natgas1);
        naturalGasList.add(natgas2);

        // mock the findAll method
        when(utilityRepository.findAllNaturalGas()).thenReturn(naturalGasList);

        // store the list of naturalgas objects
        List<NaturalGas> allNaturalGas = utilityService.getAllNaturalGas();

        // assert correct number of naturalgas objects are returned
        assertEquals(2, naturalGasList.size());
        assertEquals(2, allNaturalGas.size());

        // assert that the first naturalgas object in the list has the expected values
        assertEquals("Natural Gas 1", allNaturalGas.get(0).getName());
        assertEquals(100.0, allNaturalGas.get(0).getPressure());

        // assert that the second naturalgas object in the list has the expected values
        assertEquals("Natural Gas 2", allNaturalGas.get(1).getName());
        assertEquals(100.0, allNaturalGas.get(1).getPressure());
    }

    // test get one utility by id
    @Test
    public void testGetUtilityById() {
        // create a new utility object
        Utility utility = new Electricity("Electricity 1", 230.0);

        utility.setUnitOfMeasurement("kWh");

        // mock the findById method
        when(utilityRepository.findById(1L)).thenReturn(Optional.of(utility));

        // call the getUtilityById method
        Utility singleUtility = utilityService.getUtility(1L);

        // check that the returned object is not null
        assertNotNull(singleUtility);

    }

}