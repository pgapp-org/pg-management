package com.pgapp.request.tenant;

import lombok.Data;

@Data
public class ComplaintFeedbackRequest {
    private Long complaintId;
    private Integer rating;
    private String feedback;
}