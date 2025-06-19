package com.example.btba.busticketbookingapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Seat {
    private String seatNumber;
    private boolean isBooked;
}
