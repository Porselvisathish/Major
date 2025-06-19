package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.dto.PassengerDto;
import com.example.btba.busticketbookingapplication.dto.SavePassengerDto;
import com.example.btba.busticketbookingapplication.mapper.PassengerMapper;
import com.example.btba.busticketbookingapplication.model.Passenger;
import com.example.btba.busticketbookingapplication.repo.PassengerRepo;
import com.example.btba.busticketbookingapplication.repo.RouteRepo;
import com.example.btba.busticketbookingapplication.repo.StopRepo;
import com.example.btba.busticketbookingapplication.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepo passengerRepo;
    private final StopRepo stopRepo;
    private final RouteRepo routeRepo;
//    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

//    /**
//     * A Service method to verify passenger credentials.
//     *
//     * @param user Object having username and password for validation.
//     * @return True if valid user and vice versa.
//     */
//    @Override
//    public boolean verify(UserCredentialDto user) {
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            user.getUsername(),
//                            user.getPassword()
//                    )
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return authentication.isAuthenticated();
//        } catch(Exception e){
//            return false;
//        }
//    }

    /**
     * A service method to get the first name of the passenger who has logged in.
     *
     * @param email email id of the passenger
     * @return      the first name of the passenger
     */
    @Override
    public String getUserFirstnameByUsername(String email) {
        Passenger fetchedPassenger = passengerRepo.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("No passenger present in our database under given email id.: " + email + ".")
        );
        return fetchedPassenger.getFirstName();
    }

    /**
     * A Service method to add a passenger.
     *
     * @param newPassenger A new passenger object.
     * @return Saved passenger dto object.
     */
    @Override
    public PassengerDto addPassenger(SavePassengerDto newPassenger) {
        Optional<Passenger> optionalPassenger = passengerRepo.findByEmail(newPassenger.getEmail());
        if(optionalPassenger.isPresent()) {
            throw new RuntimeException("Passenger already exists with given email.");
        }
        System.out.println("newPassenger before encoding: " + newPassenger);
        newPassenger.setPassword(encoder.encode(newPassenger.getPassword()));
        System.out.println("newPassenger after encoding: " + newPassenger);
        Passenger savedPassenger = passengerRepo.save(PassengerMapper.mapToPassengerFromSavePassengerDto(newPassenger));
        return PassengerMapper.mapToPassengerDto(savedPassenger);
    }

    /**
     * A Service method to get all the routes from the database.
     *
     * @return A list of all routes.
     */
    @Override
    public List<String> getAllRoutes() {
        List<String> routes = routeRepo.findDistinctRoutes();
        if (routes.isEmpty()) {
            System.out.println("No routes found from the database. Check if route table is empty.");
            return List.of("None");
        }
        return routes;
    }

    /**
     * A Service method to get all the stops from the database.
     *
     * @return A list of all stops.
     */
    @Override
    public List<String> getAllStops() {
        List<String> locations = stopRepo.findDistinctLocations();
        if (locations.isEmpty()) {
            System.out.println("No locations found from the database. Check if stop table is empty.");
            return List.of("No locations found.");
        }
        return locations;
    }
}
