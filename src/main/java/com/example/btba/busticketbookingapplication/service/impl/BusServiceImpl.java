package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.mapper.BusMapper;
import com.example.btba.busticketbookingapplication.model.Bus;
import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.model.Seat;
import com.example.btba.busticketbookingapplication.model.SeatType;
import com.example.btba.busticketbookingapplication.repo.BusBookingRepo;
import com.example.btba.busticketbookingapplication.repo.BusRepo;
import com.example.btba.busticketbookingapplication.repo.RouteRepo;
import com.example.btba.busticketbookingapplication.repo.StopRepo;
import com.example.btba.busticketbookingapplication.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BusServiceImpl implements BusService { // 7 non-overridden support methods exist
    // If any problem arises due to passing params from bus-search-result, only send busId and make busTravelDto a global variable.
    @Autowired
    private BusRepo busRepo;
    @Autowired
    private RouteRepo routeRepo;
    @Autowired
    private StopRepo stopRepo;
    @Autowired
    private BusBookingRepo busBookingRepo;

    private BusBooking busBooking;

    /**
     * A service method to get the bus data using the bus id.
     *
     * @param busId The id of the bus,
     * @return      A Bus Dto object.
     */
    @Override
    public BusDto getBusById(long busId) {
        Bus bus = busRepo.findById(busId).orElseThrow(()->new RuntimeException("Error while fetching Bus data"));
        return BusMapper.mapToBusDto(bus);
    }

    /**
     * A service method for getting a list of buses for given source and destination locations.
     *
     * @param passengerStartPoint passenger service startpoint,
     * @param passengerEndPoint   passenger service endpoint,
     * @param sourceDepartureDate date of travel,
     * @return A list of buses running between given stops.
     */
    @Override
    public List<BusTravelDto> getBusesBetweenSourceAndDestination(String passengerStartPoint, String passengerEndPoint, LocalDate sourceDepartureDate) {
        List<Bus> busList = new ArrayList<>();
        List<BusDto> busDtoList = new ArrayList<>();

        // Finding bus routes
        List<Long> routeIds = stopRepo.findRouteIdsByStops(passengerStartPoint, passengerEndPoint);
        System.out.println("Route ids: " + routeIds.toString());

        // Finding buses on the found routes
        routeIds.forEach( id -> busList.addAll(busRepo.findByRouteId(id)));

        List<BusTravelDto> busTravelDtoList = this.setBusTravelDto(passengerStartPoint, passengerEndPoint, sourceDepartureDate, busList);

        // Creating Dto list
        AtomicInteger i = new AtomicInteger(0);
        busList.forEach(bus -> {
            System.out.println("Bus "+ i.incrementAndGet() + ": " + bus);
            busDtoList.add(BusMapper.mapToBusDto(bus));
        });
        System.out.println("Bus Dtos: " + busDtoList.toString());
        System.out.println("busList: " + busList.toString());
        System.out.println("busTravelDtoList: " + busTravelDtoList.toString());
        if(busDtoList.isEmpty()) System.out.println("No buses found.");

        return busTravelDtoList;
    }

    /**
     * A non-overridden service method to set bus traveling details between given source and destination.
     *
     * @param passengerStartPoint Source location,
     * @param passengerEndPoint   Destination location,
     * @param sourceDepartureDate travel date,
     * @param busList             List of Buses between given source and destination,
     * @return                    A list of Dtos having bus traveling details.
     */
    private List<BusTravelDto> setBusTravelDto(String passengerStartPoint, String passengerEndPoint, LocalDate sourceDepartureDate, List<Bus> busList) {
        System.out.println("Setting busTravelDtoList from BusServiceImpl...");
        List<BusTravelDto> busTravelDtoList = new ArrayList<>();
        busList.forEach(bus -> {
            int availableSeats = this.setAvailableSeats(bus.getId(), bus.getSeatType(), bus.getNoOfRows(), sourceDepartureDate);
            if (availableSeats > 0) {
                BusTravelDto busTravelDto = BusTravelDto.builder()
                        .busId(bus.getId())
                        .busName(bus.getBusName())
                        .busType(this.setBusType(bus.getAirConditioned(), bus.getSeatType()))
                        .availableSeats(availableSeats)
                        .source(passengerStartPoint) // source location for passenger
                        .destination(passengerEndPoint) // destination location for passenger
                        .sourceDepartureDate(null)
                        .sourceDepartureTime(null)
                        .destinationArrivalDate(null)
                        .destinationArrivalTime(null)
                        .duration("0h 0m")
                        .baseFare(0D)
                        .build();
                Map<String, LocalDateTime> departureAndArrivalMap = this.setDepartureAndArrivalData(bus.getRoute().getId(), bus.getSourceDepartureTime(), sourceDepartureDate, passengerStartPoint, passengerEndPoint);
                busTravelDto.setSourceDepartureDate(departureAndArrivalMap.get("departureDateTime").toLocalDate());
                busTravelDto.setSourceDepartureTime(departureAndArrivalMap.get("departureDateTime").toLocalTime());
                busTravelDto.setDestinationArrivalDate(departureAndArrivalMap.get("arrivalDateTime").toLocalDate());
                busTravelDto.setDestinationArrivalTime(departureAndArrivalMap.get("arrivalDateTime").toLocalTime());
                busTravelDto.setDuration(this.getBusDuration(departureAndArrivalMap.get("departureDateTime"), departureAndArrivalMap.get("arrivalDateTime")));
                busTravelDto.setBaseFare(bus.getPricePerKm() * stopRepo.findDistanceBetweenLocations(passengerStartPoint, passengerEndPoint, bus.getRoute().getId()));
                busTravelDtoList.add(busTravelDto);
            }
        });
        System.out.println("busTravelDtoList set.");
        return busTravelDtoList;
    }

    /**
     * A non-overridden service method to get the duration string between locations for a bus.
     *
     * @param departureDateTime LocalDateTime variable having the time of departure,
     * @param arrivalDateTime   LocalDateTime variable having the time of arrival,
     * @return a String value mentioning the duration between the departure and arrival point.
     */
    @Override
    public String getBusDuration(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        String s = "";
        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        if(duration.toDays() > 0) s = s + duration.toDays() + "d ";
        s = s + duration.toHours() + "h " + (duration.toMinutes() % 60) + "m";
        return s;
    }

    /**
     * A non-overridden service method to assign the bus type e.g. AC Sleeper, Non-AC Sleeper.
     *
     * @param airConditioned boolean value mentioning if bus is air-conditioned,
     * @param seatType bus seat type.
     * @return String value mentioning the bus type
     */
    private String setBusType(Boolean airConditioned, SeatType seatType) {
        return ((airConditioned) ? "AC" : "Non-AC")+ " " + seatType.toString();
    }

    /**
     * A non-overridden service method to get the number of available seats in a bus.
     *
     * @param busId               Bus id in the database,
     * @param seatType            type of bus seat,
     * @param noOfRows            no. of rows in the bus,
     * @param sourceDepartureDate travel date,
     * @return                    the number of available seats.
     */
    private int setAvailableSeats(Long busId, SeatType seatType, int noOfRows, LocalDate sourceDepartureDate)  {
        int noOfColumns = switch (seatType) {
            case SLEEPER_2_1 -> 6; // 3 from each upper and lower deck
            case SEMI_SLEEPER_2_2 -> 4;
            default -> 5;
        };
        int totalSeats = noOfRows * noOfColumns;
        List<BusBooking> busBookingList = busBookingRepo.findAllByBusIdAndPickupDateAndIsCancelled(busId, sourceDepartureDate, false);
        AtomicInteger bookedSeats = new AtomicInteger();
        busBookingList.forEach(booking -> {
            String seatNumber = booking.getSeatNumber();
            bookedSeats.addAndGet(seatNumber.split(", ").length);
        });
        return (totalSeats- bookedSeats.get());
    }

    /**
     * A non-overridden service method to set departure and arrival data for the travel
     *
     * @param routeId                route id,
     * @param busSourceDepartureTime departure time of the bus from its service starting point,
     * @param sourceDepartureDate    date of travel
     * @param passengerStartPoint    source location for passenger
     * @param passengerEndPoint      destination location for passenger
     * @return a map with departure and arrival data for the travel.
     */
    private Map<String, LocalDateTime> setDepartureAndArrivalData(Long routeId,
                                                                  LocalTime busSourceDepartureTime,
                                                                  LocalDate sourceDepartureDate,
                                                                  String passengerStartPoint,
                                                                  String passengerEndPoint) {
        Map<String, LocalDateTime> map = new HashMap<>();

        // Finding the bus service source location
        String busSourceDepartureLocation = stopRepo.findBusSourceLocation(routeId);
        System.out.println("busSourceDepartureLocation: " + busSourceDepartureLocation);

        // Defining departure date and time from passenger start point
        LocalDateTime departureDateTime = LocalDateTime.of(sourceDepartureDate, busSourceDepartureTime);
        int travelTimeInMinutes = 0;
        if(!busSourceDepartureLocation.equals(passengerStartPoint)){
            travelTimeInMinutes = this.getTravelTimeBetweenStopsInMinutes(routeId, busSourceDepartureLocation, passengerStartPoint);
            departureDateTime = departureDateTime.plusMinutes(travelTimeInMinutes);
        }
        map.put("departureDateTime", departureDateTime);

        // Defining arrival date and time from passenger start point
        travelTimeInMinutes = this.getTravelTimeBetweenStopsInMinutes(routeId, passengerStartPoint, passengerEndPoint);
        LocalDateTime arrivalDateTime = departureDateTime.plusMinutes(travelTimeInMinutes);
        map.put("arrivalDateTime", arrivalDateTime);
        return map;
    }

    /**
     * A non-overridden service method to find the travel time between two locations. By default, considers a travel time of 1 hour for 45 km.
     *
     * @param routeId             Route Id,
     * @param passengerStartPoint Passenger service start point,
     * @param passengerEndPoint   Passenger Service end point,
     * @return The travel duration in minutes.
     */
    private int getTravelTimeBetweenStopsInMinutes(Long routeId, String passengerStartPoint, String passengerEndPoint) {
        Double distanceInKmBetweenStops = stopRepo.findDistanceBetweenLocations(passengerStartPoint, passengerEndPoint, routeId);
        return (int)Math.ceil(distanceInKmBetweenStops*60/45);
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

    @Override
    public void initializeBusBookingObject(String from, String to, String sourceDepartureDateString, String sourceDepartureTimeString) {
        LocalDate sourceDepartureDate = LocalDate.parse(sourceDepartureDateString);
        LocalTime sourceDepartureTime = LocalTime.parse(sourceDepartureTimeString);
        busBooking = new BusBooking();
        busBooking.setCancelled(false);
        System.out.println("Initialized bus booking object");
        busBooking.setPickupPoint(from);
        busBooking.setPickupDate(sourceDepartureDate);
        busBooking.setPickupTime(sourceDepartureTime);
        busBooking.setDropPoint(to);
        System.out.println("Set sourceDeparture date as pickup date of bus booking object.");
    }

    /**
     * A service method to get the seat data of a bus to be shown in the webpage.
     *
     * @param busIdString               The id of the bus as a String,
     * @param sourceDepartureDateString Departure date of the bus as String,
     * @return                          A list of seats with their seat number and boolean value mentioning if the seat is booked or not.
     */
    @Override
    public List<Seat> getSeatDetails(String busIdString, String sourceDepartureDateString) {
        Long busId = Long.parseLong(busIdString);
        LocalDate sourceDepartureDate = LocalDate.parse(sourceDepartureDateString);
        Bus tempBus = busRepo.findById(busId).orElseThrow(()-> new RuntimeException("Error while fetching bus data."));
        busBooking.setBus(tempBus);
        System.out.println("Set bus details to bus booking object");
        List<Seat> seatList = new ArrayList<>();
        switch (tempBus.getSeatType()){
            case SLEEPER_2_1 -> {
                createSeatList(tempBus.getNoOfRows(), seatList, "U", "A");
                createSeatList(tempBus.getNoOfRows(), seatList, "U", "B");
                createSeatList(tempBus.getNoOfRows(), seatList, "U", "C");
                createSeatList(tempBus.getNoOfRows(), seatList, "L", "A");
                createSeatList(tempBus.getNoOfRows(), seatList, "L", "B");
                createSeatList(tempBus.getNoOfRows(), seatList, "L", "C");
            }
            case SEMI_SLEEPER_2_2 -> {
                createSeatList(tempBus.getNoOfRows(), seatList, "", "A");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "B");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "C");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "D");
            }
            case SEMI_SLEEPER_3_2 -> {
                createSeatList(tempBus.getNoOfRows(), seatList, "", "A");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "B");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "C");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "D");
                createSeatList(tempBus.getNoOfRows(), seatList, "", "E");
            }
        }
        System.out.println("seat list: " + seatList);
        List<BusBooking> busBookingList = busBookingRepo.findAllByBusIdAndPickupDateAndIsCancelled(busId, sourceDepartureDate, false);
        seatList.forEach(seat -> {
            busBookingList.forEach(temp ->{
                if(temp.getSeatNumber().contains(seat.getSeatNumber())) seat.setBooked(true);
            });
        });
        System.out.println("BusBookingList: " + busBookingList);
        return seatList;
    }

    /**
     * A non-overridden service method to create a list of seats for the getSeatDetails() method.
     *
     * @param noOfRows No. of rows in a bus,
     * @param seatList List of seats where the created seats are to be added,
     * @param prefix   Prefix String to be added to seat number,
     * @param suffix   Suffix String to be added to seat number.
     */
    private static void createSeatList(int noOfRows, List<Seat> seatList, String prefix, String suffix) {
        for(int i = 1; i <= noOfRows; i++){
            String temp = prefix + i + suffix;
            Seat seat = new Seat(temp, false);
            seatList.add(seat);
        }
    }

    /**
     * A service method to save the selected seats from the controller to the bus booking object. Data will not immediately be saved to the database.
     * Once the bus booking object is completely filled during the booking process, it'll then be saved to db.
     *
     * @param seatNumbers A list of seat numbers selected
     */
    @Override
    public void saveSeatNumbersToBusBookingObject(List<String> seatNumbers) {
        String seats = seatNumbers.get(0);
        int n = seatNumbers.size();
        for(int i = 1; i < n; i++) {
            seats += ", " + seatNumbers.get(i);
        }
        System.out.println(seats);
        busBooking.setSeatNumber(seats);
        System.out.println("Set seat numbers to bus booking object.");
    }

    /**
     * A service method to set dropping details and price to the bus booking global object.
     *
     * @return the bus booking object.
     */
    @Override
    public BusBooking getBoardingAndDroppingSummary() {
        LocalDateTime pickupDateTime = LocalDateTime.of(busBooking.getPickupDate(), busBooking.getPickupTime());
        int travelTimeInMinutes = this.getTravelTimeBetweenStopsInMinutes(busBooking.getBus().getRoute().getId(), busBooking.getPickupPoint(), busBooking.getDropPoint());
        LocalDateTime dropDateTime = pickupDateTime.plusMinutes(travelTimeInMinutes);
        busBooking.setDropDate(dropDateTime.toLocalDate());
        busBooking.setDropTime(dropDateTime.toLocalTime());
        Double pricePerTicket = busBooking.getBus().getPricePerKm() * stopRepo.findDistanceBetweenLocations(busBooking.getPickupPoint(), busBooking.getDropPoint(), busBooking.getBus().getRoute().getId());
        int ticketCount = busBooking.getSeatNumber().split(", ").length;
        busBooking.setPrice(pricePerTicket * ticketCount);
        return busBooking;
    }

    /**
     * A service method to get the passenger count in the current bus booking object.
     *
     * @return the number of passengers or seats selected.
     */
    @Override
    public int getPassengerCountFromBusBookingObject() {
        return busBooking.getSeatNumber().split(", ").length;
    }

    /**
     * A service method to set the passenger details to the current bus booking object.
     *
     * @param passenger1Name   Name of passenger 1
     * @param passenger1Age    Age of passenger 1
     * @param passenger1Gender Gender of passenger 1
     * @param passenger2Name   Name of passenger2
     * @param passenger2Age    Age of passenger 2
     * @param passenger2Gender Gender of passenger 2
     * @param passenger3Name   Name of passenger 3
     * @param passenger3Age    Age of passenger 3
     * @param passenger3Gender Gender of passenger 3
     * @param passenger4Name   Name of passenger 4
     * @param passenger4Age    Age of passenger 4
     * @param passenger4Gender Gender of passenger 4
     * @param passengerEmail   Passenger email id for booking
     * @param passengerMobile  Passenger mobile for booking
     * @param username         username of the user
     * @return the current bus booking object.
     */
    @Override
    public BusBooking savePassengerDetailsToBusBookingObject(
            String passenger1Name, String passenger1Age, String passenger1Gender,
            String passenger2Name, String passenger2Age, String passenger2Gender,
            String passenger3Name, String passenger3Age, String passenger3Gender,
            String passenger4Name, String passenger4Age, String passenger4Gender,
            String passengerEmail, String passengerMobile, String username) {
        busBooking.setPassenger1Name(passenger1Name);
        busBooking.setPassenger1Age(Integer.parseInt(passenger1Age));
        busBooking.setPassenger1Gender(passenger1Gender);
        busBooking.setPassengerEmail(passengerEmail);
        busBooking.setPassengerMobile(passengerMobile);
        if(passenger2Name != null) {
            busBooking.setPassenger2Name(passenger2Name);
            busBooking.setPassenger2Age(Integer.parseInt(passenger2Age));
            busBooking.setPassenger2Gender(passenger2Gender);
        }

        if(passenger3Name != null) {
            busBooking.setPassenger3Name(passenger3Name);
            busBooking.setPassenger3Age(Integer.parseInt(passenger3Age));
            busBooking.setPassenger3Gender(passenger3Gender);
        }

        if(passenger4Name != null) {
            busBooking.setPassenger4Name(passenger4Name);
            busBooking.setPassenger4Age(Integer.parseInt(passenger4Age));
            busBooking.setPassenger4Gender(passenger4Gender);
        }
        System.out.println("set passenger details to bus booking object");
        System.out.println(busBooking);
        busBooking.setBookedBy(username);
        return busBooking;
    }

    /**
     * A service method to save the bus booking object and confirm the booking.
     *
     * @return a boolean value based on the outcome.
     */
    @Override
    public boolean ConfirmBooking() {
        busBooking.setBookedAt(LocalDateTime.now());
        try{
            BusBooking savedBusBooking = busBookingRepo.save(busBooking);
        } catch (Exception e) {
            System.err.println("Error while booking!");
            System.err.println(e.getStackTrace());
            return false;
        }
        return true;
    }

}
