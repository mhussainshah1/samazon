package com.company.samazon.controller;

import com.company.samazon.models.User;
import com.company.samazon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //////////////////////////////////////// New User, Login(CUSTOMER)
    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/newuser")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "userform";
    }

    @PostMapping("/newuser")
    public String processUser(@Valid @ModelAttribute("user") User user) {
        user.setPassword(userService.encode(user.getPassword()));
        userService.saveCustomer(user);
        return "redirect:/login";
    }
}
