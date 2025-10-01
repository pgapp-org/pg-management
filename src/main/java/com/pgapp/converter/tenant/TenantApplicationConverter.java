package com.pgapp.converter.tenant;

// package: com.pgapp.converter

import com.pgapp.enums.ApplicationStatus;
import com.pgapp.request.tenant.TenantApplicationRequest;
import com.pgapp.response.tenant.TenantApplicationResponse;
import com.pgapp.entity.TenantApplication;
import com.pgapp.entity.Room;

public class TenantApplicationConverter {

    // Entity → Response DTO
    public static TenantApplicationResponse toResponse(TenantApplication app) {
        Room room = app.getRoom();
        return TenantApplicationResponse.builder()
                .id(app.getId())
                .status(app.getStatus() != null ? app.getStatus().name() : null)
                .tenantId(app.getTenant() != null ? app.getTenant().getId() : null)
                .tenantName(app.getTenant() != null ? app.getTenant().getName() : null)
                .pgId(app.getPg() != null ? app.getPg().getId() : null)
                .pgName(app.getPg() != null ? app.getPg().getName() : null)
                .roomId(room != null ? room.getId() : null)
                .roomNumber(room != null ? room.getRoomNumber() : null)
                .roomCapacity(room != null ? room.getCapacity() : null)
                .roomOccupiedBeds(room != null ? room.getOccupiedBeds() : null)
                .roomVacantBeds(room != null && room.getCapacity() != null && room.getOccupiedBeds() != null
                        ? room.getCapacity() - room.getOccupiedBeds()
                        : null)
                .bedNumber(app.getBedNumber())
                .foodOpted(app.getFoodOpted())
                .build();
    }

    // Request DTO → Entity
    public static TenantApplication fromRequest(TenantApplicationRequest request,
                                                com.pgapp.entity.Tenant tenant,
                                                com.pgapp.entity.PG pg,
                                                com.pgapp.entity.Room room) {
        return TenantApplication.builder()
                .tenant(tenant)
                .pg(pg)
                .room(room)
                .bedNumber(request.getBedNumber())
                .foodOpted(request.getFoodOpted())
                .status(ApplicationStatus.PENDING)
                .build();
    }
}
