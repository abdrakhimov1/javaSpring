package com.abdrakhimov.startup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    UserRepository repository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    User newUser(@RequestBody User user) {
        return repository.save(user);
    }
}
