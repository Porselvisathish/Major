package com.example.btba.busticketbookingapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String busNumber;
    private String busName;
    @Column(columnDefinition = "time(0)") // to avoid any fractional seconds in the database
    private LocalTime sourceDepartureTime;
    private Boolean airConditioned;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    private Double pricePerKm;
    private int noOfRows; // No. of rows of seat in the bus
    @ManyToOne
    @JoinColumn(name="route_id")
    private Route route;
}
