package com.abdrakhimov.startup;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String login;
    private String password;
}
