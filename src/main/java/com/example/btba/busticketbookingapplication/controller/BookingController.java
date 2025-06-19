package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/booking")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * An api (mvc method) to find all the bookings done by the current user.
     *
     * @param authentication an Authentication interface object
     * @param model          a Model interface object
     * @return               Booking list view.
     */
    // http://localhost:8081/booking/bookingList
    @GetMapping(value = "/bookingList")
    public String showBookings(Authentication authentication, Model model){
        System.out.println("Rendering previous bookings...");
        List<List<BusBooking>> busBookingList = bookingService.getBusBookingListByUsername(authentication.getName());
        model.addAttribute("upcomingBusBookingListIsEmpty" ,busBookingList.get(0).isEmpty());
        model.addAttribute("upcomingBusBookingList", busBookingList.get(0));
        model.addAttribute("completedBusBookingListIsEmpty" ,busBookingList.get(1).isEmpty());
        model.addAttribute("completedBusBookingList", busBookingList.get(1));
        return "booking-list";
    }
}
