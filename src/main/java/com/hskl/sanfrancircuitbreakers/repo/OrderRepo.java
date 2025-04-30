package com.hskl.sanfrancircuitbreakers.repo;

import com.hskl.sanfrancircuitbreakers.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
}
