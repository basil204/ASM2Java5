package com.example.ASM2.repository;

import com.example.ASM2.model.Log;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
  @Query(value = "SELECT * FROM Log WHERE user_id = :user_id", nativeQuery = true)
  List<Log> findByUserId(@Param("user_id") int user_id);
}
