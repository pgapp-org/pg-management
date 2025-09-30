package com.pgapp.converter;


import com.pgapp.converter.tenant.TenantConverter;
import com.pgapp.request.owner.RoomRequest;
import com.pgapp.response.BedResponse;
import com.pgapp.response.RoomResponse;
import com.pgapp.entity.Room;

import java.util.stream.Collectors;

public class RoomConverter {

    public static Room toEntity(RoomRequest request) {
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPrice(request.getPrice());
        room.setDoorPosition(request.getDoorPosition());
        room.setRoom360ViewUrl(request.getRoom360ViewUrl());
        return room;
    }

//    public static RoomResponse toResponse(Room room) {
//        RoomResponse res = new RoomResponse();
//        res.setId(room.getId());
//        res.setRoomNumber(room.getRoomNumber());
//        res.setCapacity(room.getCapacity());
//        res.setOccupiedBeds(room.getOccupiedBeds());
//        res.setPrice(room.getPrice());
//        res.setDoorPosition(room.getDoorPosition());
//        res.setRoom360ViewUrl(room.getRoom360ViewUrl());
//        res.setBedIds(room.getBeds().stream().map(b -> b.getId()).collect(Collectors.toList()));
//        // 🔹 Convert nested beds
//        if (room.getBeds() != null) {
//            res.setBeds(room.getBeds().stream()
//                    .map(BedConverter::toResponse)
//                    .collect(Collectors.toList()));
//        }
//        return res;
//    }
public static RoomResponse toResponse(Room room) {
    RoomResponse res = new RoomResponse();
    res.setId(room.getId());
    res.setRoomNumber(room.getRoomNumber());
    res.setCapacity(room.getCapacity());
    res.setOccupiedBeds(room.getOccupiedBeds());
    res.setPrice(room.getPrice());
    res.setDoorPosition(room.getDoorPosition());
    res.setRoom360ViewUrl(room.getRoom360ViewUrl());

    // Map beds
    if (room.getBeds() != null) {
        res.setBeds(room.getBeds().stream().map(bed -> {
            BedResponse bedRes = BedConverter.toResponse(bed);

            // 🔹 Assign tenant to bed if a tenant exists in the room
            if (room.getTenants() != null) {
                room.getTenants().stream()
                        .filter(t -> t.getBedNumber() != null
                                && t.getBedNumber().equals(String.valueOf(bed.getBedNumber())))
                        .findFirst()
                        .ifPresent(t -> bedRes.setTenant(TenantConverter.toResponse(t)));
            }

            return bedRes;
        }).collect(Collectors.toList()));
    }

    // Set bedIds for frontend if needed
    res.setBedIds(room.getBeds().stream().map(b -> b.getId()).collect(Collectors.toList()));

    return res;
}

}
