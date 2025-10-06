package com.pgapp.response;

import com.pgapp.enums.AmenityType;
import lombok.Data;

import java.util.List;

@Data
public class PGResponse {
    private Long id;
    private String name;
    private String houseNo;
    private String area;
    private String city;
    private String state;
    private String pincode;
    private String foodPolicy;
    private Double foodFee;
    private Double advanceAmount;       // used if variableAdvance == false
    private boolean variableAdvance;    // true = room-specific

    private Integer noticePeriodMonths;

    private Double pricePerDayWithFood;
    private Double pricePerDayWithoutFood;
    private boolean shortTermAllowed;
    private List<AmenityType> amenities;
    private List<String> images;

    private List<FloorResponse> floors;
}
