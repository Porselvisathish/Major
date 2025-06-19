package com.example.btba.busticketbookingapplication.dto;

import com.example.btba.busticketbookingapplication.model.Route;
import com.example.btba.busticketbookingapplication.model.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class BusDto {
    private Long id;
    private String busNumber;
    private String busName;
    private LocalTime sourceDepartureTime;
    private Boolean airConditioned;
    private SeatType seatType;
    private int noOfRows;
    private Route route;
}
// pricePerKm unavailable against model class
