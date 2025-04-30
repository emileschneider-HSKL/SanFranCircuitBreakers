package com.hskl.sanfrancircuitbreakers.repo;

import com.hskl.sanfrancircuitbreakers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
