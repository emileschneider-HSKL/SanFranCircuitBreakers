package com.hskl.sanfrancircuitbreakers.repo;

import com.hskl.sanfrancircuitbreakers.models.Adress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepo extends JpaRepository<Adress, Long> {
}
