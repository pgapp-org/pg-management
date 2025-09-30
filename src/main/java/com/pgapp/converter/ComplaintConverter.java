package com.pgapp.converter;


import com.pgapp.entity.Complaint;
import com.pgapp.response.ComplaintResponse;

public class ComplaintConverter {

    public static ComplaintResponse toResponse(Complaint complaint) {
        ComplaintResponse response = new ComplaintResponse();
        response.setId(complaint.getId());
        response.setTenantId(complaint.getTenant().getId());
        response.setTenantName(complaint.getTenant().getName());
        response.setPgId(complaint.getPg().getId());
        response.setPgName(complaint.getPg().getName());
        response.setRoomNumber(complaint.getRoomNumber());
        response.setCategory(complaint.getCategory());
        response.setDescription(complaint.getDescription());
        response.setStatus(complaint.getStatus());
        response.setOwnerComments(complaint.getOwnerComments());
        response.setCreatedAt(complaint.getCreatedAt());
        response.setResolvedAt(complaint.getResolvedAt());
        response.setImageUrl(complaint.getImageUrl());
        response.setTenantRating(complaint.getTenantRating());
        response.setTenantFeedback(complaint.getTenantFeedback());
        return response;
    }

}
