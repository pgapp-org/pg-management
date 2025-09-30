package com.pgapp.service;

import com.pgapp.converter.ComplaintConverter;
import com.pgapp.entity.Complaint;
import com.pgapp.entity.Tenant;
import com.pgapp.repository.ComplaintRepository;
import com.pgapp.repository.TenantRepository;
//import com.pgapp.request.ComplaintFeedbackRequest;
//import com.pgapp.request.ComplaintRequest;
//import com.pgapp.request.ComplaintUpdateRequest;
import com.pgapp.request.owner.ComplaintUpdateRequest;
import com.pgapp.request.tenant.ComplaintFeedbackRequest;
import com.pgapp.request.tenant.ComplaintRequest;
import com.pgapp.response.ComplaintResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final TenantRepository tenantRepository;
//
//    public ComplaintResponse createComplaint(ComplaintRequest request) {
//        Tenant tenant = tenantRepository.findById(request.getTenantId())
//                .orElseThrow(() -> new RuntimeException("Tenant not found"));
//
//        Complaint complaint = Complaint.builder()
//                .tenant(tenant)
//                .category(request.getCategory())
//                .description(request.getDescription())
//                .imageUrl(request.getImageUrl())
//                .status("IN_PROGRESS") // default
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        complaintRepository.save(complaint);
//        return ComplaintConverter.toResponse(complaint);
//    }
public ComplaintResponse createComplaint(ComplaintRequest request) {
    Tenant tenant = tenantRepository.findById(request.getTenantId())
            .orElseThrow(() -> new RuntimeException("Tenant not found"));

    if (tenant.getPg() == null || !tenant.getPg().getId().equals(request.getPgId())) {
        throw new RuntimeException("Tenant not assigned to this PG, cannot raise complaint");
    }

    Complaint complaint = Complaint.builder()
            .tenant(tenant)
            .pg(tenant.getPg())
            .roomNumber(request.getRoomNumber())
            .category(request.getCategory())
            .description(request.getDescription())
            .imageUrl(request.getImageUrl())
            .status("IN_PROGRESS")
            .createdAt(LocalDateTime.now())
            .build();

    complaintRepository.save(complaint);
    return ComplaintConverter.toResponse(complaint);
}

    public ComplaintResponse updateComplaint(ComplaintUpdateRequest request) {
        Complaint complaint = complaintRepository.findById(request.getComplaintId())
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(request.getStatus());
        complaint.setOwnerComments(request.getOwnerComments());

        if ("RESOLVED".equalsIgnoreCase(request.getStatus())) {
            complaint.setResolvedAt(LocalDateTime.now());
        }

        complaintRepository.save(complaint);
        return ComplaintConverter.toResponse(complaint);
    }

    public ComplaintResponse addTenantFeedback(ComplaintFeedbackRequest request) {
        Complaint complaint = complaintRepository.findById(request.getComplaintId())
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setTenantRating(request.getRating());
        complaint.setTenantFeedback(request.getFeedback());

        complaintRepository.save(complaint);
        return ComplaintConverter.toResponse(complaint);
    }

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll()
                .stream()
                .map(ComplaintConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getComplaintsByTenant(Long tenantId) {
        return complaintRepository.findByTenantId(tenantId)
                .stream()
                .map(ComplaintConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getComplaintsByPg(Long pgId) {
        return complaintRepository.findByPgId(pgId)
                .stream()
                .map(ComplaintConverter::toResponse)
                .collect(Collectors.toList());
    }
}
