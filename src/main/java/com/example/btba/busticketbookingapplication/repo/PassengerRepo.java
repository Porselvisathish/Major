package com.example.btba.busticketbookingapplication.repo;

import com.example.btba.busticketbookingapplication.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail (String username);
}
