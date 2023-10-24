package com.hari.cloud.app.controller;

import com.hari.cloud.app.dao.Assignment;
import com.hari.cloud.app.dto.AssignmentDto;
import com.hari.cloud.app.service.AssignmentService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    @GetMapping("/v1/assignments")
    public ResponseEntity getAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        if(assignments == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity(assignments, HttpStatus.OK);
        }
    }

    @GetMapping("/v1/assignments/{id}")
    public ResponseEntity getAssignment(@PathVariable("id") String id) {
        Assignment assignment = assignmentService.getAssignmentBy(id);
        if(assignment == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity(assignment, HttpStatus.OK);
        }
    }

    @PostMapping("/v1/assignments")
    public ResponseEntity createAssignment(@RequestBody @Valid AssignmentDto assignmentDto) {
        Assignment assignment = assignmentService.createAssignment(assignmentDto);
        if(assignment == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity(assignment, HttpStatus.CREATED);
        }
    }

    @PutMapping("/v1/assignments/{id}")
    public ResponseEntity updateAssignment(@PathVariable("id") String id, @RequestBody @Valid AssignmentDto assignmentDto) {
        Assignment assignment = assignmentService.updateAssignment(assignmentDto, id);
        if(assignment == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/v1/assignments/{id}")
    public ResponseEntity deleteAssignment(@PathVariable("id") String id) {
        Boolean isSuccessful = assignmentService.deleteAssignmentBy(id);
        if(isSuccessful) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}

