package com.pgapp.request.tenant;

import com.pgapp.entity.ComplaintCategory;
import lombok.Data;

@Data
public class ComplaintRequest {
    private Long tenantId;
    private Long pgId;          // must belong to tenant’s PG
    private String roomNumber;  // tenant must specify
    private ComplaintCategory category;
    private String description;
    private String imageUrl; // optional
}