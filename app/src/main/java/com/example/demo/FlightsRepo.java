package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface FlightsRepo extends CrudRepository<Flights, Long> {

    @Query(value = "SELECT * FROM flights WHERE pilot_id = :id", nativeQuery = true)
    public Iterable<Flights> retrieveFlights(Long id);

    @Query(value = "SELECT * FROM flights WHERE date = :date", nativeQuery = true)
    public Iterable<Flights> retrieveFlightsByDate(Date date);

    @Query(value = "SELECT * FROM flights WHERE pilot_id is null", nativeQuery = true)
    public Iterable<Flights> retrieveEmpty();


}
