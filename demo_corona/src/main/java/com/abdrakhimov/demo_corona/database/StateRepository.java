package com.abdrakhimov.demo_corona.database;

import com.abdrakhimov.demo_corona.models.Country;
import com.abdrakhimov.demo_corona.models.State;
import org.springframework.data.repository.CrudRepository;

public interface StateRepository extends CrudRepository<State, Long> {

}