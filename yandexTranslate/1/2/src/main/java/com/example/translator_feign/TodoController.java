package com.example.translator_feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class TodoController {

    @Autowired
    TodoClient todoClient;

    @GetMapping()
    public List<TodoModel> getTodos()
    {
        return todoClient.getTodos();
    }

}