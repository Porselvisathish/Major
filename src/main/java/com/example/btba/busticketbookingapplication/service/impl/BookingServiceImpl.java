package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.repo.BusBookingRepo;
import com.example.btba.busticketbookingapplication.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BusBookingRepo busBookingRepo;

    /**
     * A service method to show the bookings done by a user.
     *
     * @param username email id of the user
     * @return the list of bookings made by the user
     */
    @Override
    public List<List<BusBooking>> getBusBookingListByUsername(String username) {
        List<BusBooking> busBookingList = busBookingRepo.findAllByBookedBy(username);
        List<List<BusBooking>> seperatedBookingList = new ArrayList<>();
        List<BusBooking> upcomingBookingList = new ArrayList<>();
        List<BusBooking> completedBookingList = new ArrayList<>();
        busBookingList.forEach( busBooking -> {
            LocalDateTime pickupDateTime = LocalDateTime.of(busBooking.getPickupDate(), busBooking.getPickupTime());
            if(!busBooking.isCancelled() && pickupDateTime.isAfter(LocalDateTime.now())) {
                if(upcomingBookingList.isEmpty()) {
                    upcomingBookingList.add(busBooking);
                }
                else {
                    int n = upcomingBookingList.size();
                    for(int i = 0; i < n; i++) {
                        if (pickupDateTime.isBefore(LocalDateTime.of(upcomingBookingList.get(i).getPickupDate(), upcomingBookingList.get(i).getPickupTime()))) {
                            upcomingBookingList.add(i, busBooking);
                            break;
                        }
                    }
                    if(n == upcomingBookingList.size()) upcomingBookingList.add(busBooking);
                }
            } else {
                if(completedBookingList.isEmpty()) {
                    completedBookingList.add(busBooking);
                }
                else {
                    int n = completedBookingList.size();
                    for (int i = 0; i < n; i++) {
                        if (pickupDateTime.isAfter(LocalDateTime.of(completedBookingList.get(i).getPickupDate(), completedBookingList.get(i).getPickupTime()))) {
                            completedBookingList.add(i, busBooking);
                            break;
                        }
                    }
                    if (n == completedBookingList.size()) completedBookingList.add(busBooking);
                }
            }
        });
        System.out.println("Upcoming bookings: " + upcomingBookingList);
        System.out.println("Completed bookings: " + completedBookingList);
        seperatedBookingList.add(upcomingBookingList);
        seperatedBookingList.add(completedBookingList);
        return seperatedBookingList;
    }
}
