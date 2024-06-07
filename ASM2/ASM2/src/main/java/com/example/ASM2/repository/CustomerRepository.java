package com.example.ASM2.repository;

import com.example.ASM2.model.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query("SELECT p FROM Customer p join fetch p.user")
  List<Customer> findAlls();

}
