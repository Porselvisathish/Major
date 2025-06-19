package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.PassengerDto;
import com.example.btba.busticketbookingapplication.dto.SavePassengerDto;

import java.util.List;

public interface PassengerService {
//    boolean verify(UserCredentialDto user);

    String getUserFirstnameByUsername(String name);

    PassengerDto addPassenger(SavePassengerDto newPassenger);

    List<String> getAllRoutes();

    List<String> getAllStops();
}
