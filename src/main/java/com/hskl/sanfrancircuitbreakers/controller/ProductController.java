package com.hskl.sanfrancircuitbreakers.controller;

import com.hskl.sanfrancircuitbreakers.models.Product;
import com.hskl.sanfrancircuitbreakers.models.Supplier;
import com.hskl.sanfrancircuitbreakers.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//------------------------------------Abgeschlossen------------------------------------//
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    //Gibt alle Produkte zurück
    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {
            List<ProductResponse> responseList = productRepo.findAll()
                    .stream()
                    .map(p -> new ProductResponse(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getPrice(),
                            p.getSupplier() != null ? p.getSupplier().getName() : null,
                            p.getCategory() != null ? p.getCategory().getName() : null
                    ))
                    .toList();

            if (responseList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Gibt alle Produkte in der bestimmt Price Range zurück
    @GetMapping("/search-by-price")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam double min,
            @RequestParam double max
    ) {
        try {
            List<ProductResponse> responseList = productRepo.findByPriceBetween(min, max)
                    .stream()
                    .map(p -> new ProductResponse(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getPrice(),
                            p.getSupplier() != null ? p.getSupplier().getName() : null,
                            p.getCategory() != null ? p.getCategory().getName() : null
                    ))
                    .toList();

            if (responseList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Gibt das bestimmte Produkt der ID zurück
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Optional<Product> productData = productRepo.findById(id);

        if (productData.isPresent()) {
            Product p = productData.get();

            String supplierName = p.getSupplier() != null ? p.getSupplier().getName() : null;
            String categoryName = p.getCategory() != null ? p.getCategory().getName() : null;

            ProductResponse response = new ProductResponse(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    supplierName,
                    categoryName
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Gibt den Lieferanten des Produktes an
    @GetMapping("/{id}/supplier")
    public ResponseEntity<SupplierResponse> getSupplierOfProduct(@PathVariable Long id) {
        Optional<Product> productData = productRepo.findById(id);

        if (productData.isPresent()) {
            Supplier supplier = productData.get().getSupplier();

            if (supplier != null) {
                SupplierResponse response = new SupplierResponse(
                        supplier.getId(),
                        supplier.getName(),
                        supplier.getAddress()
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Produkt einfügen
    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product productObj = productRepo.save(product);
        return new ResponseEntity<>(productObj, HttpStatus.OK);
    }

    //Produkt überschreiben
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody Product newProductData) {
        Optional<Product> oldProductData = productRepo.findById(id);

        if (oldProductData.isPresent()) {
            Product updatedProduct = oldProductData.get();
            updatedProduct.setName(newProductData.getName());
            updatedProduct.setDescription(newProductData.getDescription());
            updatedProduct.setPrice(newProductData.getPrice());
            updatedProduct.setSupplier(newProductData.getSupplier()); // falls Supplier auch aktualisiert werden soll

            Product savedProduct = productRepo.save(updatedProduct);
            return new ResponseEntity<>(savedProduct, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Output Response Model
    public record SupplierResponse(
            Long id,
            String name,
            String address
    ) {
    }

    public record ProductResponse(
            Long id,
            String name,
            String description,
            double price,
            String supplierName,
            String categoryName
    ) {
    }

    /*
    @DeleteMapping("/deleteProductById/{id}")
    public ResponseEntity<HttpStatus> deleteProductByID(@PathVariable Long id) {
        productRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
     */
}
