package com.example.btba.busticketbookingapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BusBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="bus_id")
    private Bus bus;

    private String seatNumber;

    private String pickupPoint;
    private LocalDate pickupDate;
    @Column(columnDefinition = "time(0)") // to avoid any fractional seconds in the database
    private LocalTime pickupTime;

    private String dropPoint;
    private LocalDate dropDate;
    @Column(columnDefinition = "time(0)")
    private LocalTime dropTime;

    private String passenger1Name;
    private Integer passenger1Age;
    private String passenger1Gender;
    private String passenger2Name;
    private Integer passenger2Age;
    private String passenger2Gender;
    private String passenger3Name;
    private Integer passenger3Age;
    private String passenger3Gender;
    private String passenger4Name;
    private Integer passenger4Age;
    private String passenger4Gender;
    private String passengerEmail;
    private String passengerMobile;
    private Double price;

    @Column(columnDefinition = "timestamp(0)") // to avoid any fractional seconds in the database
    private LocalDateTime bookedAt;
    private String bookedBy;
    private boolean isCancelled;
}
