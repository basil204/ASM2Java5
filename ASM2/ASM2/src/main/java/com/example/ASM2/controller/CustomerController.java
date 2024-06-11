package com.example.ASM2.controller;

import com.example.ASM2.model.Customer;
import com.example.ASM2.model.Log;
import com.example.ASM2.model.Users;
import com.example.ASM2.repository.CustomerRepository;
import com.example.ASM2.repository.LogRepository;
import com.example.ASM2.repository.UsersRepository;
import com.example.ASM2.storage.SessionManager;
import jakarta.validation.Valid;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/customer")
public class CustomerController {

  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  LogRepository logRepository;
  @Autowired
  UsersRepository usersRepository;
  private List<Customer> customers = new ArrayList<Customer>();
  private List<Users> users = new ArrayList<Users>();


  @ModelAttribute("datacustomer")
  public List<Customer> getCustomer() {
    customers = customerRepository.findAlls();
    return customers;
  }


  @ModelAttribute("dataUser")
  public List<Users> getUser() {
    users = usersRepository.findAll();
    return users;
  }

  @GetMapping("/list")
  public String list(Log log) {
    if (SessionManager.isLogin() == false) {
      return "redirect:/";
    }
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setAction("url /list");
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setNotes("truy cap list");
    logRepository.save(log);
    return "Customer/index";
  }

  @GetMapping("/add")
  public String add(Model model, Log log, Users user) {
    if (SessionManager.isLogin() == false) {
      return "redirect:/";
    }
model.addAttribute("data", user);
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(ids);
    log.setAction("url /add");
    log.setNotes("truy cap add");
    logRepository.save(log);
    return "Customer/add";
  }

  @GetMapping("/refund")
  public String refund() {
    return "Customer/refund";
  }


  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model, Log log) {
    if(SessionManager.isLogin() == false){
      return "redirect:/";
    }
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    Customer customer = customerRepository.findById(id).orElseThrow();
    model.addAttribute("data", customer);
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /edit/" + customer.getId());
    log.setNotes("truy cap edit");
    logRepository.save(log);
    return "Customer/edit";
  }

  @PostMapping("/save")
  public String save(@RequestParam Long user_id, Log log,@Valid Customer customer, BindingResult bindingResult, Model model) {
    Users user = usersRepository.findById(user_id).orElse(null);
    if(bindingResult.hasErrors()){
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : fieldErrors) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    model.addAttribute("errors",errors);
    model.addAttribute("data", customer);
return "Customer/add";
  }


    if (user != null) {
      customer.setUser(user);
      customer.setTrangthai(1);
      int ids = Math.toIntExact(SessionManager.getUsers().getId());
      log.setUser_id(ids);
      log.setTimestamp(new Timestamp(System.currentTimeMillis()));
      log.setAction("url /save/" + customer.getId());
      log.setNotes("update: " + customer.getName() +"|"+customer.getEmail()+ "|"+customer.getUser().getName());
      logRepository.save(log);
      customerRepository.save(customer);
    }
    return "redirect:/customer/list";
  }

  @PostMapping("/update/{id}")
  public String update(@PathVariable Long id, Log log, @RequestParam Long user_id,
      @ModelAttribute @Valid Customer customer1, BindingResult bindingResult, Model model) {

    Users user = usersRepository.findById(user_id)
        .orElseThrow();

    if(bindingResult.hasErrors()){
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      Map<String, String> errors = new HashMap<String, String>();
      for (FieldError fieldError : fieldErrors) {
        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      }
      customer1.setUser(user);
      model.addAttribute("errors",errors);
      model.addAttribute("data", customer1);
      return "Customer/edit";
    }
    if (SessionManager.isLogin() == false) {
      return "redirect:/";
    }
    Customer customer2 = customerRepository.findById(id)
        .orElseThrow();
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    String namecu = customer2.getName();
    String mailcu = customer2.getEmail();
    String nameadmin = customer2.getUser().getName();
    customer2.setName(customer1.getName());
    customer2.setEmail(customer1.getEmail());
    customer2.setUser(user);
    customer2.setTrangthai(1);
    customerRepository.save(customer2);
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    log.setAction("url /update/" + customer1.getId());
    log.setNotes("Before update: " + namecu + " | " + mailcu + "|"+nameadmin+" => After update: "
        + customer1.getName() + " | " + customer1.getEmail());
    logRepository.save(log);
    return "redirect:/customer/list";
  }


  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id,Log log) {
    Customer customer = customerRepository.findById(id).orElseThrow();
    int ids = Math.toIntExact(SessionManager.getUsers().getId());
    if (customer.getTrangthai() == 1) {
      customer.setTrangthai(0);
    } else {
      customer.setTrangthai(1);
    }
    log.setAction("url /delete/"+customer.getId() );
    log.setNotes("Xoa thanh cong " + customer.getId());
    log.setUser_id(ids);
    log.setTimestamp(new Timestamp(System.currentTimeMillis()));
    logRepository.save(log);
    customerRepository.delete(customer);
    return "redirect:/customer/list";
  }


}
