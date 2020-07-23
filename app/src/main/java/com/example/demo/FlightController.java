package com.example.demo;

import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class FlightController {

    private final FlightsRepo flightsRepo;
    private final PlanesRepo planesRepo;
    private final UsersRepo usersRepo;

    public FlightController(FlightsRepo flightsRepo, PlanesRepo planesRepo
    , UsersRepo usersRepo){
        this.flightsRepo = flightsRepo;
        this.planesRepo = planesRepo;
        this.usersRepo = usersRepo;
    }

    @GetMapping("/")
    public String home(){
        return "AAnd we're up";
    }


    @GetMapping("/users")
    public Iterable<Users> listUsers(){
        return this.usersRepo.findAll();
    }

    @GetMapping("/flights")
    public Iterable<Flights> listFlights(){
        return this.flightsRepo.findAll();
    }

    @GetMapping("/planes")
    public Iterable<Planes> listPlanes(){
        return this.planesRepo.findAll();
    }

    @PostMapping("/flights")
    public Flights createFlights(@RequestBody Flights flights){
        return this.flightsRepo.save(flights);
    }

    @PutMapping("/flights/{id}")
    public Flights pickUpFlight(@RequestBody Users users, @PathVariable Long id){
        Flights flightToChange = this.flightsRepo.findById(id).get();
        flightToChange.setPilotId(users.getId());
        return this.flightsRepo.save(flightToChange);
    }

    @PostMapping("/users")
    public Users createUser(@RequestBody Users user){
        return this.usersRepo.save(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        this.usersRepo.deleteById(id);
        return "Deleted!";
    }

    @DeleteMapping("/flights/{id}")
    public String deleteFlight(@PathVariable Long id){
        this.flightsRepo.deleteById(id);
        return "Deleted!";
    }

    @GetMapping("/users/{id}")
    public Iterable<Flights> retrieveFlightsForUser(@PathVariable Long id){
        return this.flightsRepo.retrieveFlights(id);
    }

    @PatchMapping("flights/{id}")
    public Flights patchFlight(@RequestBody Flights flights, @PathVariable Long id) {
        Flights flightToPatch = this.flightsRepo.findById(id).get();
        flightToPatch.setDate(flights.getDate());
        return this.flightsRepo.save(flightToPatch);
    }


    @GetMapping("/flights/date")
    public Iterable<Flights> getFlightsByDate(@RequestParam (name = "date") String date) throws ParseException {
        Date newDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date);
        return this.flightsRepo.retrieveFlightsByDate(newDate);
    }

    @PutMapping("/flights/{id}/notes")
    public Flights addNotes(@RequestBody Flights flights, @PathVariable Long id){
        Flights flightNote = this.flightsRepo.findById(id).get();
        flightNote.setNotes(flights.getNotes());
        return this.flightsRepo.save(flightNote);
    }

    @PutMapping("/flights/{id}/planes")
    public Flights addPlane(@RequestBody Flights flights, @PathVariable Long id){
        Flights flightPlane = this.flightsRepo.findById(id).get();
        flightPlane.setPlaneId(flights.getPlaneId());
        return this.flightsRepo.save(flightPlane);
    }


    @GetMapping("/flights/assign")
    public String assignFlights(){
        Random random = new Random();
    Iterable<Flights> flights = this.flightsRepo.retrieveEmpty();
        System.out.println(flights);
    Iterable<Users> users = this.usersRepo.findAll();
    for (Flights flight: flights){
        LinkedList<Users> availableUsers = new LinkedList<>();
        for (Users user: users){
            boolean isAvailable = true;
            Iterable<Date> datesAssigned = this.usersRepo.retrieveDates(user.getId());

            for (Date date: datesAssigned){
                if (date.equals(flight.getDate())){
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable){
                System.out.println("Hit!");
                availableUsers.add(user);
            }

        }
        if(availableUsers.size() > 0) {
            int index = random.nextInt(availableUsers.size());
            flight.setPilotId(availableUsers.get(index).getId());
            this.flightsRepo.save(flight);
        }
    }



        return"Assigned!";
    }




}
