package com.example.ASM2.repository;

import com.example.ASM2.model.Users;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByUsername(String Username);
  public Users getUserByUsernameAndPassword(String username, String password);
}
