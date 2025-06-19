package com.example.btba.busticketbookingapplication.repo;

import com.example.btba.busticketbookingapplication.model.BusBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusBookingRepo extends JpaRepository<BusBooking, Long> {
    List<BusBooking> findAllByBusIdAndPickupDateAndIsCancelled(Long busId, LocalDate pickupDate, boolean isCancelled);
//    int countByBusIdAndPickupDateAndIsCancelled(Long busId, LocalDate pickupDate, boolean isCancelled);
    List<BusBooking> findAllByBookedBy(String username);
}
