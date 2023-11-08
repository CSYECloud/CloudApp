package com.hari.cloud.app.controller;

import com.hari.cloud.app.dao.Assignment;
import com.hari.cloud.app.dto.AssignmentDto;
import com.hari.cloud.app.service.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

import java.util.List;

@RestController
@Slf4j
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;


    public static final StatsDClient statsd = new NonBlockingStatsDClient("my.prefix", "localhost", 8125);

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    @GetMapping("/v1/assignments")
    public ResponseEntity getAssignments() {
        long startTime = System.currentTimeMillis();
        statsd.incrementCounter("totalAPICalls");
        log.info("Get assignments API invoked");
        List<Assignment> assignments = assignmentService.getAllAssignments();
        statsd.recordExecutionTime("execution-latency", System.currentTimeMillis()-startTime);
        if(assignments == null) {
            log.info("Responded to get assignment with forbidden");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            log.info("Responded to get assignment with success");
            return new ResponseEntity(assignments, HttpStatus.OK);
        }
    }

    @GetMapping("/v1/assignments/{id}")
    public ResponseEntity getAssignment(@PathVariable("id") String id) {
        long startTime = System.currentTimeMillis();
        statsd.incrementCounter("totalAPICalls");
        Assignment assignment = assignmentService.getAssignmentBy(id);
        log.info("Get assignment with id API invoked");
        statsd.recordExecutionTime("execution-latency", System.currentTimeMillis()-startTime);
        if(assignment == null) {
            log.info("Responded to get assignment with id with forbidden");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            log.info("Responded to get assignment with id with success");
            return new ResponseEntity(assignment, HttpStatus.OK);
        }
    }

    @PostMapping("/v1/assignments")
    public ResponseEntity createAssignment(@RequestBody @Valid AssignmentDto assignmentDto) throws PSQLException {
        long startTime = System.currentTimeMillis();
        statsd.incrementCounter("totalAPICalls");
        Assignment assignment = assignmentService.createAssignment(assignmentDto);
        log.info("Post assignment with API invoked");
        statsd.recordExecutionTime("execution-latency", System.currentTimeMillis()-startTime);
        if(assignment == null) {
            log.info("Responded to post assignment with forbidden");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            log.info("Responded to post assignment with created");
            return new ResponseEntity(assignment, HttpStatus.CREATED);
        }
    }

    @PutMapping("/v1/assignments/{id}")
    public ResponseEntity updateAssignment(@PathVariable("id") String id, @RequestBody @Valid AssignmentDto assignmentDto) {
        long startTime = System.currentTimeMillis();
        statsd.incrementCounter("totalAPICalls");
        Assignment assignment = assignmentService.updateAssignment(assignmentDto, id);
        log.info("Put assignment with API invoked");
        statsd.recordExecutionTime("execution-latency", System.currentTimeMillis()-startTime);
        if(assignment == null) {
            log.info("Responded to put assignment with not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            log.info("Responded to put assignment with success");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/v1/assignments/{id}")
    public ResponseEntity deleteAssignment(@PathVariable("id") String id) {
        long startTime = System.currentTimeMillis();
        statsd.incrementCounter("totalAPICalls");
        Boolean isSuccessful = assignmentService.deleteAssignmentBy(id);
        log.info("Delete assignment with API invoked");
        statsd.recordExecutionTime("execution-latency", System.currentTimeMillis()-startTime);
        if(isSuccessful) {
            log.info("Responded to delete assignment with success");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            log.info("Responded to delete assignment with not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}

