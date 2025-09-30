package com.pgapp.request.owner;

import lombok.Data;

@Data
public class ComplaintUpdateRequest {
    private Long complaintId;
    private String status; // IN_PROGRESS or RESOLVED
    private String ownerComments;
}
