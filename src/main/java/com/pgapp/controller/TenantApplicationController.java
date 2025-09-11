package com.pgapp.controller;

import com.pgapp.dto.ApplyRequestDTO;
import com.pgapp.dto.TenantApplicationDTO;
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

//    @PostMapping("/apply")
//    public ResponseEntity<TenantApplicationDTO> applyForRoom(
//            @RequestParam Long tenantId,
//            @RequestParam Long pgId,
//            @RequestParam Long roomId) {
//        TenantApplicationDTO dto = applicationService.applyForRoom(tenantId, pgId, roomId);
//        return ResponseEntity.ok(dto);
//    }

    @PostMapping("/apply")
    public ResponseEntity<TenantApplicationDTO> applyForRoom(@RequestBody ApplyRequestDTO request) {
        TenantApplicationDTO dto = applicationService.applyForRoomByDetails(
                request.getTenantId(),
                request.getPgName(),
                request.getFloorNumber(),
                request.getRoomNumber()
        );
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/pg/{pgId}")
    public ResponseEntity<List<TenantApplicationDTO>> getApplicationsForPG(@PathVariable Long pgId) {
        return ResponseEntity.ok(applicationService.getApplicationsForPG(pgId));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<TenantApplicationDTO>> getApplicationsForTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(applicationService.getApplicationsForTenant(tenantId));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<TenantApplicationDTO> updateApplicationStatus(@PathVariable Long applicationId,
                                                                        @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, status));
    }
}
