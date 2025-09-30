package com.pgapp.request.tenant;

import com.pgapp.response.tenant.DailyGuestAllocationDTO;
import lombok.Data;

import java.util.List;

@Data
public class DailyApproveRequestDTO {
    private List<DailyGuestAllocationDTO> allocations;
}
