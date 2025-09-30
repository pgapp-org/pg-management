package com.pgapp.controller;


import com.pgapp.request.owner.ComplaintUpdateRequest;
import com.pgapp.request.tenant.ComplaintFeedbackRequest;
import com.pgapp.request.tenant.ComplaintRequest;
import com.pgapp.response.ComplaintResponse;
import com.pgapp.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ComplaintResponse createComplaint(@RequestBody ComplaintRequest request) {
        return complaintService.createComplaint(request);
    }

    @PutMapping("/update")
    public ComplaintResponse updateComplaint(@RequestBody ComplaintUpdateRequest request) {
        return complaintService.updateComplaint(request);
    }

    @PutMapping("/feedback")
    public ComplaintResponse addFeedback(@RequestBody ComplaintFeedbackRequest request) {
        return complaintService.addTenantFeedback(request);
    }

    @GetMapping
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<ComplaintResponse> getComplaintsByTenant(@PathVariable Long tenantId) {
        return complaintService.getComplaintsByTenant(tenantId);
    }

    @GetMapping("/pg/{pgId}")
    public List<ComplaintResponse> getComplaintsByPg(@PathVariable Long pgId) {
        return complaintService.getComplaintsByPg(pgId);
    }

}
