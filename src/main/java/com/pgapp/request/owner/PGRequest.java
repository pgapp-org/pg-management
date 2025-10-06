package com.pgapp.request.owner;

import com.pgapp.enums.AmenityType;
import lombok.Data;

import java.util.List;

@Data
public class PGRequest {
    private String name;
    private String houseNo;
    private String area;
    private String city;
    private String state;
    private String pincode;

    private String foodPolicy; // COMPULSORY, OPTIONAL, NOT_PROVIDED
    private Double foodFee;


    private Double advanceAmount;        // used if variableAdvance == false
    private boolean variableAdvance;     // true = room-specific advance


    private Integer noticePeriodMonths;

    private Double pricePerDayWithFood;
    private Double pricePerDayWithoutFood;
    private boolean shortTermAllowed;
    private List<AmenityType> amenities;
}
