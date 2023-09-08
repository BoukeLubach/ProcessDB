package com.wes.processdb.business;


import com.wes.processdb.data.entity.Utility;
import com.wes.processdb.data.entity.utilities.Electricity;
import com.wes.processdb.data.entity.utilities.NaturalGas;
import com.wes.processdb.data.entity.utilities.Water;
import com.wes.processdb.data.entity.utilities.NonCombustionGas;
import com.wes.processdb.web.requests.ElectricityRequest;
import com.wes.processdb.web.requests.NaturalGasRequest;
import com.wes.processdb.web.requests.WaterRequest;
import com.wes.processdb.web.requests.NonCombustionGasRequest;

import java.util.List;



public interface UtilityService {
    
    Utility saveUtility(Utility utility);
    Utility createUtilityFromRequest(String category, Object request);


    NaturalGas saveNaturalGas(NaturalGas naturalGas);
    NaturalGas createNaturalGasFromRequest(NaturalGasRequest naturalGasRequest); 

    Electricity saveElectricity(Electricity electricity);
    Electricity createElectricityFromRequest(ElectricityRequest electricityRequest);

    Water saveWater(Water water);
    Water createWaterFromRequest(WaterRequest waterRequest);

    NonCombustionGas saveNonCombustionGas(NonCombustionGas nonCombustionGas);
    NonCombustionGas createNonCombustionGasFromRequest(NonCombustionGasRequest nonCombustionGasRequest);

    Utility getUtility(Long id);
    
    List<Electricity> getAllElectricity();
    List<NaturalGas> getAllNaturalGas();
    List<Water> getAllWater();
    List<NonCombustionGas> getAllNonCombustionGas();

    List<Utility> getUtilitiesByProcessId(Long processId);
    List<Utility> getAllUtilities();

    void deleteUtility(Long id);

    
    // Process updateProcess(Long processId, ProcessRequest processRequest);
    // void deleteProcess(Long processId);
    // List<Process> getProcesses();

}


