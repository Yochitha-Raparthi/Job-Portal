package com.application.job.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.job.portal.model.Interview;
import com.application.job.portal.service.InterviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/interview")
public class InterviewController {
	@Autowired
    private InterviewService interviewService;

    // 1. Schedule a new interview
    @PostMapping("/schedule")
    public Interview scheduleInterview(@Valid @RequestBody Interview interview) {
        return interviewService.scheduleInterview(interview);
    }

    // 2. Get all interviews
    @GetMapping("/all")
    public List<Interview> getAllInterviews() {
        return interviewService.getAllInterviews();
    }

    // 3. Get an interview by ID
    @GetMapping("/{id}")
    public Interview getInterviewById(@PathVariable Long id) {
        return interviewService.getInterviewById(id);
    }

    // 4. Get interviews for a particular application
    @GetMapping("/application/{applicationId}")
    public List<Interview> getInterviewsByApplication(
            @PathVariable Long applicationId) {
        return interviewService.getInterviewsByApplication(applicationId);
    }

    // 5. Update interview details
    @PutMapping("/update/{id}")
    public Interview updateInterview(
            @Valid @RequestBody Interview interview,
            @PathVariable Long id) {
        return interviewService.updateInterview(interview, id);
    }

    // 6. Update interview status
    @PatchMapping("/updateStatus/{id}")
    public Interview updateInterviewStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return interviewService.updateInterviewStatus(id, status);
    }

    // 7. Delete an interview
    @DeleteMapping("/delete/{id}")
    public String deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return "Interview " + id + " deleted successfully";
    }

}
