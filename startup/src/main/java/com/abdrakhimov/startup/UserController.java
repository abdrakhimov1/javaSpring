package com.abdrakhimov.startup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    UserRepository repository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    User newUser(@ModelAttribute("user") User user, Model model, BindingResult result) {
        return repository.save(user);
    }
}
