package com.pgapp.request.owner;

import com.pgapp.entity.AmenityType;
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
    private boolean shortTermAllowed;
    private List<AmenityType> amenities;
}
