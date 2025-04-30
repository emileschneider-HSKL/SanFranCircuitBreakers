package com.hskl.sanfrancircuitbreakers.controller;

import com.hskl.sanfrancircuitbreakers.models.Supplier;
import com.hskl.sanfrancircuitbreakers.repo.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//------------------------------------Abgeschlossen------------------------------------//
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepo supplierRepo;

    //Gibt den Supplier mit der bestimmten ID aus und dazu noch die Produkte
    @GetMapping("/{id}")
    public ResponseEntity<SupplierWithProductsResponse> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplierData = supplierRepo.findById(id);

        if (supplierData.isPresent()) {
            Supplier supplier = supplierData.get();

            List<SupplierWithProductsResponse.ProductResponse> productList = supplier.getProducts().stream()
                    .map(p -> new SupplierWithProductsResponse.ProductResponse(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getPrice()
                    ))
                    .toList();

            SupplierWithProductsResponse response = new SupplierWithProductsResponse(
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getAddress(),
                    productList
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Output Response Model
    public record SupplierWithProductsResponse(
            Long id,
            String name,
            String address,
            List<ProductResponse> products
    ) {
        public record ProductResponse(
                Long id,
                String name,
                String description,
                double price
        ) {
        }
    }

    /*
    @GetMapping("")
    public ResponseEntity<List<Supplier>> getallSuppliers() {
        try{
            List<Supplier> supplier = supplierRepo.findAll();

            if(supplier.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     */
}
