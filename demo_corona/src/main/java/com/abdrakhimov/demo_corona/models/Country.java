package com.abdrakhimov.demo_corona.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Country {
    private @Id
    @GeneratedValue
    Long id;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "country")
    private List<State> states;
    private String countryName;
    private int stats;
    private int prevStats;

    public Country(List<State> states, String countryName, int stats, int prevStats) {
        this.states = states;
        this.countryName = countryName;
        this.stats = stats;
        this.prevStats = prevStats;
    }
}
