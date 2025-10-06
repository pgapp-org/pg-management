package com.pgapp.converter;


import com.pgapp.enums.FoodPolicy;
import com.pgapp.request.owner.PGRequest;
import com.pgapp.response.PGResponse;
import com.pgapp.entity.PG;

import java.util.stream.Collectors;

public class PGConverter {


//    public static PG toEntity(PGRequest request) {
//        return PG.builder()
//                .name(request.getName())
//                .houseNo(request.getHouseNo())
//                .area(request.getArea())
//                .city(request.getCity())
//                .state(request.getState())
//                .pincode(request.getPincode())
//                .foodPolicy(request.getFoodPolicy() != null
//                        ? FoodPolicy.valueOf(request.getFoodPolicy())
//                        : null) // convert String -> Enum
//                .pricePerDayWithFood(request.getPricePerDayWithFood())
//                .pricePerDayWithoutFood(request.getPricePerDayWithoutFood())
//                .shortTermAllowed(request.isShortTermAllowed())
//                .amenities(request.getAmenities())
//                .build();
//    }
//
//    public static PGResponse toResponse(PG pg) {
//        PGResponse res = new PGResponse();
//        res.setId(pg.getId());
//        res.setName(pg.getName());
//        res.setHouseNo(pg.getHouseNo());
//        res.setArea(pg.getArea());
//        res.setCity(pg.getCity());
//        res.setState(pg.getState());
//        res.setPincode(pg.getPincode());
//        res.setFoodPolicy(pg.getFoodPolicy().name());
//        res.setPricePerDayWithFood(pg.getPricePerDayWithFood());
//        res.setPricePerDayWithoutFood(pg.getPricePerDayWithoutFood());
//        res.setShortTermAllowed(pg.isShortTermAllowed());
//        res.setAmenities(pg.getAmenities());
//        res.setImages(pg.getImages());
//        if (pg.getFloors() != null) {
//            res.setFloors(pg.getFloors().stream()
//                    .map(FloorConverter::toResponse)
//                    .collect(Collectors.toList()));
//        }
//        return res;
//    }

    public static PG toEntity(PGRequest request) {
        return PG.builder()
                .name(request.getName())
                .houseNo(request.getHouseNo())
                .area(request.getArea())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .foodPolicy(request.getFoodPolicy() != null
                        ? FoodPolicy.valueOf(request.getFoodPolicy())
                        : null)
                .foodFee(request.getFoodFee())
                .advanceAmount(request.getAdvanceAmount())  // default PG advance
                .variableAdvance(request.isVariableAdvance()) // new
                .noticePeriodMonths(request.getNoticePeriodMonths())
                .pricePerDayWithFood(request.getPricePerDayWithFood())
                .pricePerDayWithoutFood(request.getPricePerDayWithoutFood())
                .shortTermAllowed(request.isShortTermAllowed())
                .amenities(request.getAmenities())
                .build();
    }

    // Entity → Response DTO
    public static PGResponse toResponse(PG pg) {
        PGResponse res = new PGResponse();
        res.setId(pg.getId());
        res.setName(pg.getName());
        res.setHouseNo(pg.getHouseNo());
        res.setArea(pg.getArea());
        res.setCity(pg.getCity());
        res.setState(pg.getState());
        res.setPincode(pg.getPincode());
        res.setFoodPolicy(pg.getFoodPolicy() != null ? pg.getFoodPolicy().name() : null);

        res.setFoodFee(pg.getFoodFee());
        res.setAdvanceAmount(pg.getAdvanceAmount());  // default PG advance
        res.setVariableAdvance(pg.isVariableAdvance()); // new
        res.setNoticePeriodMonths(pg.getNoticePeriodMonths());
        res.setPricePerDayWithFood(pg.getPricePerDayWithFood());
        res.setPricePerDayWithoutFood(pg.getPricePerDayWithoutFood());
        res.setShortTermAllowed(pg.isShortTermAllowed());
        res.setAmenities(pg.getAmenities());
        res.setImages(pg.getImages());

        if (pg.getFloors() != null) {
            res.setFloors(pg.getFloors().stream()
                    .map(FloorConverter::toResponse) // ensure FloorResponse → RoomResponse includes baseRent & advanceAmount
                    .collect(Collectors.toList()));
        }
        return res;
    }

}

