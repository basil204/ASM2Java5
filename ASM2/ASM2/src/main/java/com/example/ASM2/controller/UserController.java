package com.example.ASM2.controller;

import com.example.ASM2.model.Users;
import com.example.ASM2.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/Views"})
public class UserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UsersRepository userRepository;
  private List<Users> users = new ArrayList<>();

  @ModelAttribute("datausers")
  public List<Users> fillAll() {
    users = userRepository.findAll();
    return users;
  }

  @GetMapping("/list")
  public String list() {
    LOGGER.info("List users requested");
    return "Users/index";
  }

  @GetMapping("/refund")
  public String refund() {
    LOGGER.info("List users requested");
    return "Users/refund";
  }

  @GetMapping("/add")
  public String add() {
    LOGGER.info("Add user form requested");
    return "Users/add";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    LOGGER.info("Edit user form requested for user id: {}", id);
    Users users1 = userRepository.findById(id).orElseThrow();
    model.addAttribute("users", users1);
    return "Users/edit";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    Users users1 = userRepository.findById(id).orElseThrow();
    if (users1.getTrangthai() == 1) {
      users1.setTrangthai(0);
    } else {
      users1.setTrangthai(1);
    }
    userRepository.save(users1);
    LOGGER.info("Change user status requested for user id: {}",
        "Da Khoa User : " + users1.getName());
    // Implement your status changing logic here
    return "redirect:/Views/list";
  }

  @PostMapping("/save")
  public String save(Users users) {
    String a = null;
    users.setTrangthai(1);

    users = userRepository.save(users);
    LOGGER.info("Save user requested | ID: {} | Name: {} | Pass: {}", users.getId(),
        users.getName(), users.getPassword());
    return "redirect:/Views/list";
  }

  @PostMapping("/update/{id}")
  public String update(@PathVariable Long id, Users users1) {
    LOGGER.info("Update user requested for user id: {}", id);
    Users users2 = userRepository.findById(id).orElseThrow();
    users2.setName(users1.getName());
    users2.setPassword(users1.getPassword());
    userRepository.save(users2);
    return "redirect:/Views/list";
  }
}
