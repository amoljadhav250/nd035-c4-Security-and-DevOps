package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
