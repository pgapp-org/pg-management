//package com.pgapp.converter.tenant;
//
//import com.pgapp.enums.ApplicationStatus;
//import com.pgapp.enums.FoodPolicy;
//import com.pgapp.entity.PG;
//import com.pgapp.entity.Room;
//import com.pgapp.entity.Tenant;
//import com.pgapp.entity.TenantApplication;
//import com.pgapp.request.tenant.TenantApplicationRequest;
//import com.pgapp.response.tenant.TenantApplicationResponse;
//
//public class TenantApplicationConverter {
//
//    // Entity → Response DTO
//    // Entity → Response DTO
//    // Entity → Response
//    public static TenantApplicationResponse toResponse(TenantApplication app) {
//        Room room = app.getRoom();
//        PG pg = app.getPg();
//
//        // Determine advance amount
//        Double advance = (pg != null && pg.isVariableAdvance() && room != null)
//                ? room.getAdvanceAmount()
//                : (pg != null ? pg.getAdvanceAmount() : null);
//
//        return TenantApplicationResponse.builder()
//                .id(app.getId())
//                .status(app.getStatus())
//                .tenantId(app.getTenant() != null ? app.getTenant().getId() : null)
//                .tenantName(app.getTenant() != null ? app.getTenant().getName() : null)
//                .pgId(pg != null ? pg.getId() : null)
//                .pgName(pg != null ? pg.getName() : null)
//                .roomId(room != null ? room.getId() : null)
//                .roomNumber(room != null ? room.getRoomNumber() : null)
//                .roomCapacity(room != null ? room.getCapacity() : null)
//                .roomOccupiedBeds(room != null ? room.getOccupiedBeds() : null)
//                .roomVacantBeds(room != null && room.getCapacity() != null && room.getOccupiedBeds() != null
//                        ? room.getCapacity() - room.getOccupiedBeds() : null)
//                .bedNumber(app.getBedNumber())
//                .foodOpted(app.getFoodOpted())
//                .finalMonthlyRent(app.getFinalMonthlyRent())
//                .advanceAmount(advance)
//                .build();
//    }
//
//    // Request → Entity
//    public static TenantApplication fromRequest(TenantApplicationRequest request,
//                                                Tenant tenant,
//                                                PG pg,
//                                                Room room) {
//        Double finalMonthlyRent;
//        Double advanceAmount;
//
//        if (pg.isVariableAdvance()) {
//            finalMonthlyRent = room.getBaseRent();
//            advanceAmount = room.getAdvanceAmount();
//        } else {
//            finalMonthlyRent = room.getBaseRent();
//            advanceAmount = pg.getAdvanceAmount();
//        }
//
//// add food fee if opted
//        Boolean foodOpted = null;
//        if (pg.getFoodPolicy() == FoodPolicy.COMPULSORY) {
//            foodOpted = true;
//        } else if (pg.getFoodPolicy() == FoodPolicy.OPTIONAL) {
//            foodOpted = request.getFoodOpted() != null && request.getFoodOpted();
//            finalMonthlyRent += (foodOpted ? pg.getFoodFee() : 0);
//        } else { // NOT_PROVIDED
//            foodOpted = null;
//        }
//
//// Build entity
//        return TenantApplication.builder()
//                .tenant(tenant)
//                .pg(pg)
//                .room(room)
//                .bedNumber(request.getBedNumber())
//                .foodOpted(foodOpted)
//                .finalMonthlyRent(finalMonthlyRent)
//                .advanceAmount(advanceAmount)
//                .status(ApplicationStatus.PENDING)
//                .build();
//
//    }
//    }
//
//// package: com.pgapp.converter
////
////import com.pgapp.enums.ApplicationStatus;
////import com.pgapp.request.tenant.TenantApplicationRequest;
////import com.pgapp.response.tenant.TenantApplicationResponse;
////import com.pgapp.entity.TenantApplication;
////import com.pgapp.entity.Room;
////
////public class TenantApplicationConverter {
////
////    // Entity → Response DTO
////    public static TenantApplicationResponse toResponse(TenantApplication app) {
////        Room room = app.getRoom();
////        return TenantApplicationResponse.builder()
////                .id(app.getId())
////                .status(app.getStatus() != null ? app.getStatus().name() : null)
////                .tenantId(app.getTenant() != null ? app.getTenant().getId() : null)
////                .tenantName(app.getTenant() != null ? app.getTenant().getName() : null)
////                .pgId(app.getPg() != null ? app.getPg().getId() : null)
////                .pgName(app.getPg() != null ? app.getPg().getName() : null)
////                .roomId(room != null ? room.getId() : null)
////                .roomNumber(room != null ? room.getRoomNumber() : null)
////                .roomCapacity(room != null ? room.getCapacity() : null)
////                .roomOccupiedBeds(room != null ? room.getOccupiedBeds() : null)
////                .roomVacantBeds(room != null && room.getCapacity() != null && room.getOccupiedBeds() != null
////                        ? room.getCapacity() - room.getOccupiedBeds()
////                        : null)
////                .bedNumber(app.getBedNumber())
////                .foodOpted(app.getFoodOpted())
////                .build();
////    }
////
////    // Request DTO → Entity
////    public static TenantApplication fromRequest(TenantApplicationRequest request,
////                                                com.pgapp.entity.Tenant tenant,
////                                                com.pgapp.entity.PG pg,
////                                                com.pgapp.entity.Room room) {
////        return TenantApplication.builder()
////                .tenant(tenant)
////                .pg(pg)
////                .room(room)
////                .bedNumber(request.getBedNumber())
////                .foodOpted(request.getFoodOpted())
////                .status(ApplicationStatus.PENDING)
////                .build();
////    }
////}

package com.pgapp.converter.tenant;

import com.pgapp.entity.PG;
import com.pgapp.entity.Room;
import com.pgapp.entity.Tenant;
import com.pgapp.entity.TenantApplication;
import com.pgapp.enums.FoodPolicy;
import com.pgapp.request.tenant.TenantApplicationRequest;
import com.pgapp.response.tenant.TenantApplicationResponse;

public class TenantApplicationConverter {

    // Entity → Response DTO
    public static TenantApplicationResponse toResponse(TenantApplication app) {
        Room room = app.getRoom();
        PG pg = app.getPg();

        Double advance = (pg != null && pg.isVariableAdvance() && room != null)
                ? room.getAdvanceAmount()
                : (pg != null ? pg.getAdvanceAmount() : null);

        return TenantApplicationResponse.builder()
                .id(app.getId())
                .status(app.getStatus())
                .tenantId(app.getTenant() != null ? app.getTenant().getId() : null)
                .tenantName(app.getTenant() != null ? app.getTenant().getName() : null)
                .pgId(pg != null ? pg.getId() : null)
                .pgName(pg != null ? pg.getName() : null)
                .roomId(room != null ? room.getId() : null)
                .roomNumber(room != null ? room.getRoomNumber() : null)
                .roomCapacity(room != null ? room.getCapacity() : null)
                .roomOccupiedBeds(room != null ? room.getOccupiedBeds() : null)
                .roomVacantBeds(room != null && room.getCapacity() != null && room.getOccupiedBeds() != null
                        ? room.getCapacity() - room.getOccupiedBeds() : null)
                .bedNumber(app.getBedNumber())
                .foodOpted(app.getFoodOpted())
                .finalMonthlyRent(app.getFinalMonthlyRent())
                .advanceAmount(advance)
                .checkInDate(app.getCheckInDate())
                .checkOutDate(app.getCheckOutDate())
                .tokenPaid(app.isTokenPaid())
                .advancePaid(app.isAdvancePaid())
                .refundProcessed(app.isRefundProcessed())
                .tokenAmount(app.getTokenAmount())
                .refundAmount(app.getRefundAmount())
                .hasCheckedIn(app.isHasCheckedIn())
                .build();
    }

    // Request → Entity
    public static TenantApplication fromRequest(TenantApplicationRequest request,
                                                Tenant tenant,
                                                PG pg,
                                                Room room) {
        Double finalMonthlyRent;
        Double advanceAmount;

        if (pg.isVariableAdvance()) {
            finalMonthlyRent = room.getBaseRent();
            advanceAmount = room.getAdvanceAmount();
        } else {
            finalMonthlyRent = room.getBaseRent();
            advanceAmount = pg.getAdvanceAmount();
        }

        Boolean foodOpted = null;
        if (pg.getFoodPolicy() == FoodPolicy.COMPULSORY) {
            foodOpted = true;
        } else if (pg.getFoodPolicy() == FoodPolicy.OPTIONAL) {
            foodOpted = request.getFoodOpted() != null && request.getFoodOpted();
            finalMonthlyRent += (foodOpted ? pg.getFoodFee() : 0);
        }

        return TenantApplication.builder()
                .tenant(tenant)
                .pg(pg)
                .room(room)
                .bedNumber(request.getBedNumber())
                .foodOpted(foodOpted)
                .finalMonthlyRent(finalMonthlyRent)
                .advanceAmount(advanceAmount)
                .status(com.pgapp.enums.ApplicationStatus.PENDING)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .tokenPaid(false)
                .advancePaid(false)
                .refundProcessed(false)
                .tokenAmount(null)
                .refundAmount(null)
                .build();
    }
}
