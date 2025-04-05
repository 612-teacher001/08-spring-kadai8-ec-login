package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	// SELECT * FROM customers WHERE email = ?
	Optional<Customer> findByEmail(String email);
	// SELECT * FROM customers WHERE email = :email AND password = :password 
	Optional<Customer> findByEmailAndPassword(String email, String password);
	
}
