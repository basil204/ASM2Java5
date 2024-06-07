package com.example.ASM2.controller;

import com.example.ASM2.model.Users;
import com.example.ASM2.storage.SessionManager;

import com.example.ASM2.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller

public class LoginController {

  @Autowired
  UsersRepository usersRepository;

  @GetMapping("/")
  public String index() {
    return "index.html";
  }
  

  @PostMapping("/login")
  public String loginPost(@ModelAttribute Users user) {
    Users usersLogin = usersRepository.getUserByUsernameAndPassword(user.getUsername(),
        user.getPassword());
    if (usersLogin != null) {
      SessionManager.login(usersLogin);
      return "redirect:/home";
    }
    return "index.html";
  }

  @GetMapping("/home")
  public String home() {
    if (!SessionManager.isLogin()) {
      return "redirect:/";
    }
    return "ViewLogin/index";
  }

  @GetMapping("/logout")
  public String logout() {
    SessionManager.logout();
    return "redirect:/";
  }
}
