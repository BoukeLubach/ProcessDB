package com.wes.processdb.business;

import java.util.List;


import com.wes.processdb.data.entity.Process;
import com.wes.processdb.web.requests.ProcessRequest;


public interface ProcessService {
    Process createProcess(ProcessRequest processRequest);
    Process createProcess(Process process);
    
    Process getProcess(Long processId);

    Process updateProcess(Long processId, Process process);
    Process updateProcess(Long processId, ProcessRequest processRequest);
    
    void deleteProcess(Long processId);
    List<Process> getProcesses();
    
    Process getOrCreateProcess(String name);

}
