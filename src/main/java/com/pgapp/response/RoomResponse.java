package com.pgapp.response;

import com.pgapp.enums.FoodPolicy;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private Integer capacity;
    private Integer occupiedBeds;
    private Double baseRent;           // room-specific rent
    private Double advanceAmount;      // room-specific advance if variableAdvance == true
    private String doorPosition;
    private String room360ViewUrl;
    private List<Long> bedIds;
    private FoodPolicy foodPolicy;

    // 🔹 Add nested beds
    private List<BedResponse> beds;
}