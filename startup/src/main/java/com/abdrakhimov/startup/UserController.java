package com.abdrakhimov.startup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Binding;


@RestController("users")
public class UserController {

    @Autowired
    UserRepository repository;

    @PostMapping("register")
    User newUser(@ModelAttribute("user") User user, Model model, BindingResult result) {
        return repository.save(user);
    }
}
