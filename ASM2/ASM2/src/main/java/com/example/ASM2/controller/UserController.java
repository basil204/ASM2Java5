package com.example.ASM2.controller;

import com.example.ASM2.model.Log;
import com.example.ASM2.model.Users;
import com.example.ASM2.repository.LogRepository;
import com.example.ASM2.repository.UsersRepository;
import com.example.ASM2.storage.SessionManager;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/user"})
public class UserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UsersRepository userRepository;
  @Autowired
  LogRepository logRepository;
  private List<Users> users = new ArrayList<>();

  @ModelAttribute("datausers")
  public List<Users> fillAll() {
    users = userRepository.findAll();
    return users;
  }

  @GetMapping("/list")
  public String list(Log log) {
    if(SessionManager.isLogin() == false){
      return "redirect:/";
    }
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /list/");
    log.setNotes("update: list");
    logRepository.save(log);
    return "Users/index";
  }

  @GetMapping("/refund")
  public String refund() {
    LOGGER.info("List users requested");
    return "Users/refund";
  }

  @GetMapping("/add")
  public String add(@ModelAttribute("users") Users user,Log log) {
    if(SessionManager.isLogin() == false){
      return "redirect:/";
    }
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /add" );
    log.setNotes("update: add" );
    logRepository.save(log);
    return "Users/add";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model,Log log) {
    if(SessionManager.isLogin() == false){
      return "redirect:/";
    }
    Users users1 = userRepository.findById(id).orElseThrow();
    model.addAttribute("users", users1);
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /save/" + users1.getId());
    log.setNotes("update: " + users1.getName() +"|"+users1.getUsername()+ "|"+users1.getPassword());
    logRepository.save(log);
    return "Users/edit";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    if(SessionManager.isLogin() == false){
      return "redirect:/";
    }
    Users users1 = userRepository.findById(id).orElseThrow();
    if (users1.getTrangthai() == 1) {
      users1.setTrangthai(0);
    } else {
      users1.setTrangthai(1);
    }
    userRepository.save(users1);
    return "redirect:/user/list";
  }

  @PostMapping("/save")
  public String save(@ModelAttribute @Valid Users user, BindingResult bindingResult, Model model,Log log) {
    if (bindingResult.hasErrors()) {
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      Map<String, String> errors = new HashMap<>();
      for (FieldError fieldError : fieldErrors) {
        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      }
      model.addAttribute("errors", errors);
      model.addAttribute("users", user);
      return "Users/add.html";
    }
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /save/" + user.getId());
    log.setNotes("update: " + user.getName() +"|"+user.getUsername()+ "|"+user.getPassword());
    logRepository.save(log);
    user.setTrangthai(1);
    userRepository.save(user);
    return "redirect:/user/list";
  }

  @PostMapping("/update/{id}")
  public String update(@PathVariable Long id, Users users1,Log log) {
    LOGGER.info("Update user requested for user id: {}", id);
    Users users2 = userRepository.findById(id).orElseThrow();
    String namecu = users1.getUsername();
    String password = users1.getPassword();
    users2.setName(users1.getName());
    users2.setPassword(users1.getPassword());
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /save/" + users1.getId());
    log.setNotes("defun: " + namecu +"|"+password+ "|"+users1.getName()+"update: "+"|"+users1.getPassword());
    logRepository.save(log);
    userRepository.save(users2);

    return "redirect:/user/list";
  }
}
