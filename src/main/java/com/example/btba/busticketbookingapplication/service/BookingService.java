package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.model.BusBooking;

import java.util.List;

public interface BookingService {

    List<List<BusBooking>> getBusBookingListByUsername(String email);
}
