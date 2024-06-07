package com.example.ASM2.repository;

import com.example.ASM2.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

  public Users getUserByUsernameAndPassword(String username, String password);
}
