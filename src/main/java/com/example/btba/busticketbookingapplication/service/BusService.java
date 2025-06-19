package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.model.Seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BusService {
    BusDto getBusById(long busId);
    List<BusTravelDto> getBusesBetweenSourceAndDestination(String passengerStartPoint, String passengerEndPoint, LocalDate sourceDepartureDate);

    String getBusDuration(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);

    List<String> getAllRoutes();

    List<String> getAllStops();

    void initializeBusBookingObject(String from, String to, String sourceDepartureDate, String sourceDepartureTime);

    List<Seat> getSeatDetails(String busId, String sourceDepartureDate);

    void saveSeatNumbersToBusBookingObject(List<String> seatNumbers);

    BusBooking getBoardingAndDroppingSummary();

    int getPassengerCountFromBusBookingObject();

    BusBooking savePassengerDetailsToBusBookingObject(String passenger1Name, String passenger1Age, String passenger1Gender, String passenger2Name, String passenger2Age, String passenger2Gender, String passenger3Name, String passenger3Age, String passenger3Gender, String passenger4Name, String passenger4Age, String passenger4Gender, String passengerEmail, String passengerMobile, String name);

    boolean ConfirmBooking();
}
