package com.example.ASM2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestParam;

@Entity
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank(message = "null name")
  private String name;
  @Email(message = "Email should be valid")
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@fpt\\.edu\\.vn$", message = "Email should be in the format @fpt.edu.vn")
  private String email;
  private int trangthai;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users user;

  public Customer() {
  }

  public Customer(String name, String email, Users user, int trangthai, Long id) {
    this.name = name;
    this.email = email;
    this.user = user;
    this.trangthai = trangthai;
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getTrangthai() {
    return trangthai;
  }

  public void setTrangthai(int trangthai) {
    this.trangthai = trangthai;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }
}
