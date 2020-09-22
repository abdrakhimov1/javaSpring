package com.example.rest.demo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Employee {

    private @Id
    @GeneratedValue
    Long id;
    private String name;
    private String role;

    Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    ;

}
