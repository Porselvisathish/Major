package com.example.btba.busticketbookingapplication.repo;

import com.example.btba.busticketbookingapplication.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepo extends JpaRepository<Bus, Long> {
    List<Bus> findByRouteId(Long routeId);
}
