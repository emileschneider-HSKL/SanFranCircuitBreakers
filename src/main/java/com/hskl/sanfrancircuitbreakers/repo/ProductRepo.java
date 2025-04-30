package com.hskl.sanfrancircuitbreakers.repo;

import com.hskl.sanfrancircuitbreakers.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
