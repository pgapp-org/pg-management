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
    private Double pricePerDayWithFood;
    private Double pricePerDayWithoutFood;
    private boolean shortTermAllowed;
    private List<AmenityType> amenities;
}
