package com.pgapp.request.owner;


import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private Integer capacity;
    private Double price;
    private String doorPosition;
    private String room360ViewUrl;
}