package com.abdrakhimov.demo_corona.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class State {

    private @Id
    @GeneratedValue
    Long id;
    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="country", referencedColumnName = "id")
    private Country country;
    private int stat;
    private int prevStat;

    public State(String name, Country country, int stat, int prevStat) {
        this.name = name;
        this.country = country;
        this.stat = stat;
        this.prevStat = prevStat;
    }
}
