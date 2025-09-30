package com.pgapp.response;


import lombok.Data;

import java.util.List;

@Data
public class FloorResponse {
    private Long id;
    private Integer floorNumber;

    // 🔹 Add nested rooms
    private List<RoomResponse> rooms;
}
