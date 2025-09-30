package com.pgapp.controller;

import com.pgapp.request.tenant.TenantApplicationRequest;
import com.pgapp.response.tenant.TenantApplicationResponse;
import com.pgapp.entity.ApplicationStatus;
import com.pgapp.service.TenantApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class TenantApplicationController {

    private final TenantApplicationService applicationService;

    // ✅ Apply for a PG room
    @PostMapping("/apply")
    public ResponseEntity<TenantApplicationResponse> applyForRoom(
            @RequestBody TenantApplicationRequest request) {
        TenantApplicationResponse response = applicationService.applyForRoom(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Get all applications for a PG
    @GetMapping("/pg/{pgId}")
    public ResponseEntity<List<TenantApplicationResponse>> getApplicationsForPG(
            @PathVariable Long pgId) {
        List<TenantApplicationResponse> responses = applicationService.getApplicationsForPG(pgId);
        return ResponseEntity.ok(responses);
    }

    // ✅ Get all applications for a Tenant
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<TenantApplicationResponse>> getApplicationsForTenant(
            @PathVariable Long tenantId) {
        List<TenantApplicationResponse> responses = applicationService.getApplicationsForTenant(tenantId);
        return ResponseEntity.ok(responses);
    }

    // ✅ Update application status (APPROVED / REJECTED / PENDING)
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<TenantApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {
        TenantApplicationResponse response = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(response);
    }
}
