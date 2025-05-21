package com.hskl.sanfrancircuitbreakers.services;

import com.hskl.sanfrancircuitbreakers.models.Order;
import com.hskl.sanfrancircuitbreakers.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    //REmote Dictionary Server
    @Cacheable("orders")
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
