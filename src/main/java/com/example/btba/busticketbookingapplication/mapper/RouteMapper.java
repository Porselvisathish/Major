package com.example.btba.busticketbookingapplication.mapper;

import com.example.btba.busticketbookingapplication.dto.RouteDto;
import com.example.btba.busticketbookingapplication.model.Route;

public class RouteMapper {
    public static RouteDto mapToRouteDto (Route route) {
        if (route == null) return null;
        return RouteDto.builder()
                .id(route.getId())
                .routeName(route.getRouteName())
                .build();
    }
}
