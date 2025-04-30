package com.hskl.sanfrancircuitbreakers.controller;

import com.hskl.sanfrancircuitbreakers.models.User;
import com.hskl.sanfrancircuitbreakers.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//------------------------------------Abgeschlossen------------------------------------//
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    //Benutzer erstellen
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            // Hier könnte man z.B. das Passwort hashen (BCrypt)
            User savedUser = userRepo.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Benutzer per ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepo.findById(id);
        return user
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    //Benutzer aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepo.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            // Hier ebenfalls optional: Passwort hashen
            User savedUser = userRepo.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Benutzer löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long id) {
        try {
            if (!userRepo.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
