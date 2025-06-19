package com.example.btba.busticketbookingapplication.mapper;

import com.example.btba.busticketbookingapplication.dto.PassengerDto;
import com.example.btba.busticketbookingapplication.dto.SavePassengerDto;
import com.example.btba.busticketbookingapplication.model.Passenger;

public class PassengerMapper {
    public static Passenger mapToPassengerFromSavePassengerDto(SavePassengerDto newPassenger) {
        return Passenger.builder()
                .id(null)
                .firstName(newPassenger.getFirstName())
                .lastName(newPassenger.getLastName())
                .email(newPassenger.getEmail())
                .mobile(newPassenger.getMobile())
                .age(newPassenger.getAge())
                .gender(newPassenger.getGender())
                .password(newPassenger.getPassword())
                .build();
    }

    public static PassengerDto mapToPassengerDto(Passenger passenger) {
        return PassengerDto.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .email(passenger.getEmail())
                .mobile(passenger.getMobile())
                .age(passenger.getAge())
                .gender(passenger.getGender())
                .build();
    }
}
