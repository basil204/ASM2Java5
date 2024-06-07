package com.example.ASM2.controller;

import com.example.ASM2.model.Customer;
import com.example.ASM2.model.Log;
import com.example.ASM2.model.Users;
import com.example.ASM2.repository.CustomerRepository;
import com.example.ASM2.repository.LogRepository;
import com.example.ASM2.repository.UsersRepository;
import com.example.ASM2.storage.SessionManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  private List<Customer> customers = new ArrayList<Customer>();
  private List<Users> users = new ArrayList<Users>();
  @Autowired
  private UsersRepository usersRepository;


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
    int id = Math.toIntExact(SessionManager.getUsers().getId());
    log.setUser_id(id);
    log.setAction("url /list");
    log.setNotes("truy cap list");
    log = logRepository.save(log);
    return "Customer/index";
  }

  @GetMapping("/add")
  public String add(Model model, Log log) {
    log.setAction("url /add");
    log.setNotes("truy cap add");
    log = logRepository.save(log);
    return "Customer/add";
  }

  @GetMapping("/refund")
  public String refund() {
    return "Customer/refund";
  }


  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model, Log log) {
    Customer customer = customerRepository.findById(id).orElseThrow();
    model.addAttribute("data", customer);
//    log.setAction("url /edit/" + customer.getId());
//    log.setNotes("truy cap edit");
//    log = logRepository.save(log);
    return "Customer/edit";
  }

  @PostMapping("/save")
  public String save(Customer customer, @RequestParam Long user_id, Log log) {
    Users user = usersRepository.findById(user_id).orElse(null);
    if (user != null) {
      customer.setUser(user);
      customer.setTrangthai(1);
      customerRepository.save(customer);
    }
    return "redirect:/customer/list";
  }

  @PostMapping("/update/{id}")
  public String update(@PathVariable Long id, Log log, @RequestParam Long user_id,
      Customer customer1) {
    if (SessionManager.isLogin() == false) {
      return "redirect:/";
    }
    Customer customer2 = customerRepository.findById(id)
        .orElseThrow();

    Users user = usersRepository.findById(user_id)
        .orElseThrow();
    System.out.printf("day la gif ? +++++++ " + SessionManager.getUsers().getId());
    int ids = Math.toIntExact(SessionManager.getUsers().getId());

    log.setUser_id(ids);
    String namecu = customer2.getName();
    String mailcu = customer2.getEmail();
    customer2.setName(customer1.getName());
    customer2.setEmail(customer1.getEmail());
    customer2.setUser(user);
    customer2.setTrangthai(1);
    customerRepository.save(customer2);
    log.setAction("url /update/" + customer1.getId());
    log.setNotes("Before update: " + namecu + " | " + mailcu + " => After update: "
        + customer1.getName() + " | " + customer1.getEmail());
    logRepository.save(log);
    return "redirect:/customer/list";
  }


  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    Customer customer = customerRepository.findById(id).orElseThrow();

    if (customer.getTrangthai() == 1) {
      customer.setTrangthai(0);
    } else {
      customer.setTrangthai(1);
    }
    customerRepository.save(customer);
    return "redirect:/customer/list";
  }

}
