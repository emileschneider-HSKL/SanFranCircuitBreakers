package com.hskl.sanfrancircuitbreakers.controller;

import com.hskl.sanfrancircuitbreakers.models.Category;
import com.hskl.sanfrancircuitbreakers.repo.CategoryRepo;
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
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepo categoryRepo;

    //Gibt alle Kategorien aus
    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getAllCategorysMinimal() {
        try {
            List<Category> fullCategories = categoryRepo.findAll();
            List<CategoryResponse> slimCategories = fullCategories.stream()
                    .map(cat -> new CategoryResponse(cat.getId(), cat.getName(), cat.getDescription()))
                    .toList();

            if (slimCategories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(slimCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Gibt die bestimmte Kategorie der ID aus
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryData = categoryRepo.findById(id);

        if (categoryData.isPresent()) {
            Category c = categoryData.get();
            CategoryResponse response = new CategoryResponse(c.getId(), c.getName(), c.getDescription());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Gibt alle Produkte der bestimmten Kategorie aus
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponse>> getProductsOfCategoryOnly(@PathVariable Long id) {
        Optional<Category> categoryData = categoryRepo.findById(id);

        if (categoryData.isPresent()) {
            List<ProductResponse> responseList = categoryData.get().getProducts().stream()
                    .map(p -> new ProductResponse(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getPrice()
                    ))
                    .toList();

            if (responseList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Output Response Model
    public record CategoryResponse(
            Long id,
            String name,
            String description
    ) {
    }

    public record ProductResponse(
            Long id,
            String name,
            String description,
            double price
    ) {
    }
}
