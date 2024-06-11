package com.example.ASM2.controller;

import com.example.ASM2.model.Log;
import com.example.ASM2.model.Users;
import com.example.ASM2.repository.LogRepository;
import com.example.ASM2.storage.SessionManager;

import com.example.ASM2.repository.UsersRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
@Autowired
LogRepository logRepository;
  private List<Log> logs = new ArrayList<>();

  @GetMapping("/")
  public String index() {
    return "index.html";
  }
  @ModelAttribute("datalog")
  public List<Log> getLog() {
    Long userId;

    // Check if SessionManager.getUsers() is null
    if (SessionManager.getUsers() != null) {
      userId = SessionManager.getUsers().getId();
    } else {
      userId = 1L; // Set a temporary default user ID, adjust as needed
    }

    // Check if userId is null
    if (userId == null) {
      return Collections.emptyList(); // Return an empty list if user_id is null
    }

    int user_id = Math.toIntExact(userId);
    return logRepository.findByUserId(user_id);
  }

  @PostMapping("/login")
  public String loginPost(@ModelAttribute Users user, Log log) {
    Users usersLogin = usersRepository.getUserByUsernameAndPassword(user.getUsername(),
        user.getPassword());
    if (usersLogin != null) {
      SessionManager.login(usersLogin);
      int ids = Math.toIntExact(SessionManager.getUsers().getId());

      if (user != null) {
        log.setUser_id(ids);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        log.setAction("url /Login/" + usersLogin.getId());
        log.setNotes("update: " + usersLogin.getName() +"|"+usersLogin.getUsername()+ "|"+usersLogin.getPassword());
        logRepository.save(log);
      }
      return "redirect:/home";
    }
    return "index.html";
  }

  @GetMapping("/home")
  public String home(Users user,Model model) {
    if (!SessionManager.isLogin()) {
      return "redirect:/";
    }
model.addAttribute("name", SessionManager.getUsers().getName());
    return "ViewLogin/index";
  }

  @GetMapping("/logout")
  public String logout() {
    SessionManager.logout();
    return "redirect:/";
  }
}
