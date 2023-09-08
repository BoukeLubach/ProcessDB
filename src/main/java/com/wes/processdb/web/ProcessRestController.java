package com.wes.processdb.web;

import com.wes.processdb.business.ProcessService;
import com.wes.processdb.data.entity.Process;
import com.wes.processdb.web.requests.ProcessRequest;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.stream.Collectors;
import com.wes.processdb.business.exception.ValidationException;

@RestController
public class ProcessRestController {

    private final ProcessService processService;

    public ProcessRestController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> createProcess(@Valid @RequestBody ProcessRequest processRequest, BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        if (!errors.isEmpty()) {
            handleFieldErrors(errors);
        }

        
        Process processToReturn = processService.createProcess(processRequest);
        return new ResponseEntity<>(processToReturn, HttpStatus.CREATED);

    }

    

    @GetMapping("/process")
    public ResponseEntity<List<Process>> getAllProcesses() {
        return new ResponseEntity<>(processService.getProcesses(), HttpStatus.OK);
    }

    @GetMapping("/process/{processId}")
    public ResponseEntity<Process> getProcessById(@PathVariable Long processId) {
        Process processToReturn = processService.getProcess(processId);

        return new ResponseEntity<>(processToReturn, HttpStatus.OK);
    }

    @DeleteMapping("/process/{processId}")
    public ResponseEntity<Object> deleteProcess(@PathVariable Long processId) {
        processService.deleteProcess(processId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/process/{processId}")
    public ResponseEntity<Process> updateProcess(@PathVariable Long processId,
            @RequestBody ProcessRequest processRequest) {
        return new ResponseEntity<>(processService.updateProcess(processId, processRequest), HttpStatus.OK);
    }

    @GetMapping("/process/commodity")
    private String getCommodity() {
        String uri = "https://commodities-api.com/api/timeseries?access_key=187251eb3by1l1knesqnp547c19adaeyg3zspqs2qapon34y719j6xww34gl&start_date=2022-06-01&end_date=2023-01-01&symbols=NG";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;

    }

    private void handleFieldErrors(List<FieldError> errors) throws ValidationException {
        String errorMessage = errors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        throw new ValidationException(errorMessage);
    }


   
}
