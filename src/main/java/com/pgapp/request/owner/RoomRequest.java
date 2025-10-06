package com.pgapp.request.owner;


import com.pgapp.enums.FoodPolicy;
import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private Integer capacity;
    private Double baseRent;       // room-specific rent
    private Double advanceAmount;  // room-specific advance if PG.variableAdvance == true
    private String doorPosition;
    private String room360ViewUrl;
    private FoodPolicy foodPolicy;
}