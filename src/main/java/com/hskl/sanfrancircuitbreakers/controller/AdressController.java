package com.hskl.sanfrancircuitbreakers.controller;

import com.hskl.sanfrancircuitbreakers.models.Adress;
import com.hskl.sanfrancircuitbreakers.repo.AdressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//------------------------------------ Abgeschlossen ------------------------------------//
@RestController
@RequestMapping("/adress")
public class AdressController {

    @Autowired
    private AdressRepo adressRepo;

    // Holt alle Adressen
    @GetMapping
    public ResponseEntity<List<Adress>> getAllAdresses() {
        List<Adress> adresses = adressRepo.findAll();
        if (adresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(adresses, HttpStatus.OK);
    }

    // Neue Adresse anlegen
    @PostMapping
    public ResponseEntity<Adress> createAdress(@RequestBody Adress adress) {
        try {
            Adress saved = adressRepo.save(adress);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Bestehende Adresse aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Adress> updateAdress(@PathVariable Long id, @RequestBody Adress newAdressData) {
        Optional<Adress> existing = adressRepo.findById(id);
        if (existing.isPresent()) {
            Adress a = existing.get();
            a.setStreet(newAdressData.getStreet());
            a.setCity(newAdressData.getCity());
            a.setState(newAdressData.getState());
            a.setCountry(newAdressData.getCountry());
            a.setZipcode(newAdressData.getZipcode());

            return new ResponseEntity<>(adressRepo.save(a), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Adresse löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAdress(@PathVariable Long id) {
        try {
            adressRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
