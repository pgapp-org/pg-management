package com.pgapp.response;

import lombok.Data;

import java.util.List;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private Integer capacity;
    private Integer occupiedBeds;
    private Double price;
    private String doorPosition;
    private String room360ViewUrl;
    private List<Long> bedIds;

    // 🔹 Add nested beds
    private List<BedResponse> beds;
}