package com.pgapp.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PGRoomResponse {
    private Long roomId;
    private String roomNumber;
    private Long floorId;
    private int capacity;
    private int occupiedBeds;
}