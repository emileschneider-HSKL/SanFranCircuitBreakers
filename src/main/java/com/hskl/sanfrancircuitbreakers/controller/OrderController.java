package com.hskl.sanfrancircuitbreakers.controller;

import com.hskl.sanfrancircuitbreakers.models.Order;
import com.hskl.sanfrancircuitbreakers.repo.OrderRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//------------------------------------ Abgeschlossen ------------------------------------//
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepo orderRepo;

    // Gibt alle Bestellungen zurück
    @CircuitBreaker(name = "getAllOrdersCB", fallbackMethod = "fallbackGetAll")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(required = false) Boolean fail) {
        if (Boolean.TRUE.equals(fail)) {
            throw new RuntimeException("Manuell ausgelöster Testfehler 🚨");
        }

        List<Order> orders = orderRepo.findAll();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    // Gibt eine bestimmte Bestellung nach ID zurück
    @CircuitBreaker(name = "getOrderByIdCB", fallbackMethod = "fallbackGetById")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepo.findById(id);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Neue Bestellung anlegen
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
        try {
            Order saved = orderRepo.save(newOrder);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Bestehende Bestellung patchen (teilweise aktualisieren)
    @PatchMapping("/{id}")
    public ResponseEntity<Order> patchOrder(@PathVariable Long id, @RequestBody Order patchData) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if (optionalOrder.isPresent()) {
            Order existing = optionalOrder.get();

            // Nur die übergebenen Felder patchen
            if (patchData.getStatus() != null) {
                existing.setStatus(patchData.getStatus());
            }
            if (patchData.getShippingAdress() != null) {
                existing.setShippingAdress(patchData.getShippingAdress());
            }
            if (patchData.getUser() != null) {
                existing.setUser(patchData.getUser());
            }
            if (patchData.getProducts() != null && !patchData.getProducts().isEmpty()) {
                existing.setProducts(patchData.getProducts());
            }
            if (patchData.getCreatedAt() != null) {
                existing.setCreatedAt(patchData.getCreatedAt());
            }

            return new ResponseEntity<>(orderRepo.save(existing), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Order>> fallbackGetAll(Throwable t) {
        System.out.println("⚠️ fallbackGetAll: " + t.getMessage());
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<Order> fallbackGetById(Long id, Throwable t) {
        System.out.println("⚠️ fallbackGetById: " + t.getMessage());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
