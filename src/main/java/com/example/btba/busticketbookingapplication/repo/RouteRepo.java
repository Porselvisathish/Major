package com.example.btba.busticketbookingapplication.repo;

import com.example.btba.busticketbookingapplication.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepo extends JpaRepository<Route, Long> {
    @Query("select distinct routeName from Route")
    List<String> findDistinctRoutes();

}
