package com.example.btba.busticketbookingapplication.mapper;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.model.Bus;

public class BusMapper {
    public static BusDto mapToBusDto(Bus bus) {
        if (bus == null) return null;
        return BusDto.builder()
                .id(bus.getId())
                .busNumber(bus.getBusNumber())
                .busName(bus.getBusName())
                .sourceDepartureTime(bus.getSourceDepartureTime())
                .airConditioned(bus.getAirConditioned())
                .seatType(bus.getSeatType())
                .noOfRows(bus.getNoOfRows())
                .route(bus.getRoute())
                .build();
    }
}
