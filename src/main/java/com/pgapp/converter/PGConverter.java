package com.pgapp.converter;


import com.pgapp.request.owner.PGRequest;
import com.pgapp.response.PGResponse;
import com.pgapp.entity.PG;

import java.util.stream.Collectors;

public class PGConverter {

    public static PG toEntity(PGRequest request) {
        return PG.builder()
                .name(request.getName())
                .houseNo(request.getHouseNo())
                .area(request.getArea())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .shortTermAllowed(request.isShortTermAllowed())
                .amenities(request.getAmenities())
                .build();
    }

    public static PGResponse toResponse(PG pg) {
        PGResponse res = new PGResponse();
        res.setId(pg.getId());
        res.setName(pg.getName());
        res.setHouseNo(pg.getHouseNo());
        res.setArea(pg.getArea());
        res.setCity(pg.getCity());
        res.setState(pg.getState());
        res.setPincode(pg.getPincode());
        res.setShortTermAllowed(pg.isShortTermAllowed());
        res.setAmenities(pg.getAmenities());
        res.setImages(pg.getImages());
        if (pg.getFloors() != null) {
            res.setFloors(pg.getFloors().stream()
                    .map(FloorConverter::toResponse)
                    .collect(Collectors.toList()));
        }
        return res;
    }
}

