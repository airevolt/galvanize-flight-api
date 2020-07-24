package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface UsersRepo extends CrudRepository<Users, Long> {


    @Query(value = "SELECT flights.date FROM users JOIN flights ON users.id = flights.pilot_id WHERE users.id = :id", nativeQuery = true)
    public Iterable<Date> retrieveDates(Long id);

    @Query(value = "SELECT * FROM users WHERE is_pilot = 1", nativeQuery = true)
    public Iterable<Users> getPilots();
}