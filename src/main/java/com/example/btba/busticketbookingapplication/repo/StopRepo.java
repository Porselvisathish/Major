package com.example.btba.busticketbookingapplication.repo;

import com.example.btba.busticketbookingapplication.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepo extends JpaRepository<Stop, Long> {
    @Query("select distinct location from Stop")
    List<String> findDistinctLocations();

    // Custom query to find routes by source and destination stops
    @Query("select distinct s1.route.id from Stop s1 join Stop s2 on s1.route.id = s2.route.id where s1.location like concat('%', :source, '%') AND s2.location like concat('%', :destination, '%') AND s1.sequence < s2.sequence")
    List<Long> findRouteIdsByStops(@Param("source") String passengerStartPoint, @Param("destination") String passengerEndPoint);

    @Query("select s.location from Stop s where s.route.id = :routeId and s.sequence = 1")
    String findBusSourceLocation(@Param("routeId") Long routeId);

    @Query("select s2.distanceCovered - s1.distanceCovered from Stop s1, Stop s2 where s1.location = :source and s1.route.id = :routeId and s2.location = :destination and s2.route.id = :routeId")
    Double findDistanceBetweenLocations(@Param("source") String passengerStartPoint, @Param("destination") String passengerEndPoint, @Param("routeId") Long routeId);
}
