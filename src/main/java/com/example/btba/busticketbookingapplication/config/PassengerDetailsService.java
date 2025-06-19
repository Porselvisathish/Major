package com.example.btba.busticketbookingapplication.config;

import com.example.btba.busticketbookingapplication.model.Passenger;
import com.example.btba.busticketbookingapplication.repo.PassengerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerDetailsService implements UserDetailsService {
    @Autowired
    private PassengerRepo passengerRepo;

    public PassengerDetailsService() {}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Passenger> optionalPassenger = passengerRepo.findByEmail(username);
        if (optionalPassenger.isEmpty()){
            throw new UsernameNotFoundException("Invalid username");
        }
        return new PassengerDetails(optionalPassenger.get());
    }
}
