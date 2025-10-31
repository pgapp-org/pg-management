package com.pgapp.converter;


import com.pgapp.converter.tenant.TenantConverter;
import com.pgapp.request.owner.RoomRequest;
import com.pgapp.response.BedResponse;
import com.pgapp.response.RoomResponse;
import com.pgapp.entity.Room;

import java.util.stream.Collectors;

public class RoomConverter {



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

    public static Room toEntity(RoomRequest request) {
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setBaseRent(request.getBaseRent()); // rename price → baseRent
        room.setAdvanceAmount(request.getAdvanceAmount());
        room.setDoorPosition(request.getDoorPosition());
        room.setRoom360ViewUrl(request.getRoom360ViewUrl());
        return room;
    }

    public static RoomResponse toResponse(Room room) {
        RoomResponse res = new RoomResponse();
        res.setId(room.getId());
        res.setRoomNumber(room.getRoomNumber());
        res.setCapacity(room.getCapacity());
        res.setOccupiedBeds(room.getOccupiedBeds());
        res.setBaseRent(room.getBaseRent());
        res.setAdvanceAmount(room.getAdvanceAmount()); // room-specific advance
        res.setDoorPosition(room.getDoorPosition());
        res.setRoom360ViewUrl(room.getRoom360ViewUrl());

        if (room.getBeds() != null) {
            res.setBeds(room.getBeds().stream()
                    .map(BedConverter::toResponse)
                    .collect(Collectors.toList()));
            res.setBedIds(room.getBeds().stream().map(b -> b.getId()).collect(Collectors.toList()));
        }
        if (room.getPg() != null) {
            res.setPgId(room.getPg().getId());
            res.setPgName(room.getPg().getName());
        }

        return res;
    }

}
