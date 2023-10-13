package com.hari.cloud.app.service;

import com.hari.cloud.app.dao.Assignment;
import com.hari.cloud.app.dao.User;
import com.hari.cloud.app.dto.AssignmentDto;
import com.hari.cloud.app.repository.AssignmentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Assignment> getAllAssignments() {
        return (List<Assignment>) assignmentRepository.findAll();
    }

    public Assignment createAssignment(AssignmentDto assignmentDto) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.getUserBy(email);
        Assignment assignment = new Assignment();
        assignment.setName(assignmentDto.name);
        assignment.setNumOfAttempts(assignmentDto.num_of_attempts);
        assignment.setPoints(assignmentDto.points);
        assignment.setDeadline(assignmentDto.deadline);
        assignment.setAssignmentCreated(new Date());
        assignment.setAssignmentUpdated(new Date());
        assignment.setUser(user);
        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentBy(String id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if(assignmentRepository.findById(id).isPresent()) {
            Assignment assignment = assignmentRepository.findById(id).get();
            // Only return records belonging to the current user
            if(assignment.user.getEmail().equals(email)) return assignment;
        }
        return null;
    }

    public Assignment updateAssignment(AssignmentDto updatedAssignment, String id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if(!assignmentRepository.findById(id).isPresent()) {
            return null;
        }
        Assignment assignment = assignmentRepository.findById(id).get();
        // Only update records belonging to the current user
        if(!assignment.user.getEmail().equals(email)) return null;

        assignment.setName(updatedAssignment.name);
        assignment.setNumOfAttempts(updatedAssignment.num_of_attempts);
        assignment.setPoints(updatedAssignment.points);
        assignment.setDeadline(updatedAssignment.deadline);
        assignment.setAssignmentUpdated(new Date());
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Boolean deleteAssignmentBy(String id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if(!assignmentRepository.findById(id).isPresent()) {
            return false;
        }
        Assignment assignment = assignmentRepository.findById(id).get();
        // Only delete records belonging to the current user
        if(!assignment.user.getEmail().equals(email)) return false;
        entityManager.remove(assignment);
        entityManager.flush();
        return true;
    }
}
