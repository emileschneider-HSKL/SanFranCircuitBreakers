package com.hskl.sanfrancircuitbreakers.repo;

import com.hskl.sanfrancircuitbreakers.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
}
