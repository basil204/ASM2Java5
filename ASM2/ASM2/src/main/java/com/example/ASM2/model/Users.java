package com.example.ASM2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class Users {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
  @NotBlank(message = "null name")
    private String name;
  @NotBlank(message = "null username")
    private String username;
  @NotBlank(message = "null password")
   private String password;
    private int trangthai;
    public Users() {
    }

    public Users(Long id, String name, String username, String password, int trangthai) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.trangthai = trangthai;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
}
