package com.pgapp.response;

import com.pgapp.entity.ComplaintCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintResponse {
    private Long id;
    private Long tenantId;
    private String tenantName;
    private Long pgId;
    private String pgName;
    private String roomNumber;
    private ComplaintCategory category;
    private String description;
    private String status;
    private String ownerComments;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private String imageUrl;
    private Integer tenantRating;
    private String tenantFeedback;
}
