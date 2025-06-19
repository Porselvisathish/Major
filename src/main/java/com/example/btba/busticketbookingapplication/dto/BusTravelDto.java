package com.example.btba.busticketbookingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class BusTravelDto {
    private Long busId;
    private String busName;
    private String busType;
    private int availableSeats;
    private String source;
    private String destination;
    private LocalDate sourceDepartureDate;
    private LocalTime sourceDepartureTime;
    private LocalDate destinationArrivalDate;
    private LocalTime destinationArrivalTime;
    private String duration;
    private Double baseFare;
}
