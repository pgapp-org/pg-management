//package com.pgapp.response;
//
//import com.pgapp.enums.PaymentStatus;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Data
//public class RoomDetailsResponse {
//    private Long roomId;
//    private String roomNumber;
//    private int capacity;
//    private List<BedDetails> beds;
//
//    @Data
//    public static class BedDetails {
//        private Long bedId;
//        private int bedNumber;
//        private boolean occupied;
//
//        // Tenant info (if bed is occupied)
//        private Long tenantId;
//        private String tenantName;
//        private String phoneNumber;
//
//        // Tenant application info
//        private Long tenantApplicationId;
//        private LocalDate checkInDate;
//        private boolean appliedForCheckOut;
//
//        // Payment info for current month
//        private PaymentStatus rentStatus; // PAID / PENDING / null
//        private Double rentAmount;
//    }
//}
