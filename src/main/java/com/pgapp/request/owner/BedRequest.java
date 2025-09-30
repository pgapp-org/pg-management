package com.pgapp.request.owner;


import lombok.Data;
import java.time.LocalDate;

@Data
public class BedRequest {
    private Integer bedNumber;
    private boolean occupied;
    private boolean shortTermOccupied;
    private LocalDate shortTermUntil;
}
