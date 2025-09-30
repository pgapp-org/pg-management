package com.pgapp.converter;

import com.pgapp.request.owner.FloorRequest;
import com.pgapp.response.FloorResponse;
import com.pgapp.entity.Floor;

import java.util.stream.Collectors;

public class FloorConverter {

    public static Floor toEntity(FloorRequest request) {
        Floor floor = new Floor();
        floor.setFloorNumber(request.getFloorNumber());
        return floor;
    }

    public static FloorResponse toResponse(Floor floor) {
        FloorResponse res = new FloorResponse();
        res.setId(floor.getId());
        res.setFloorNumber(floor.getFloorNumber());


        // 🔹 Convert nested rooms
        if (floor.getRooms() != null) {
            res.setRooms(floor.getRooms().stream()
                    .map(RoomConverter::toResponse)
                    .collect(Collectors.toList()));
        }

        return res;
    }
}
