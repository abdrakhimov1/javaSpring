package com.abdrakhimov.demo_corona.database;

import com.abdrakhimov.demo_corona.models.Country;
import com.abdrakhimov.demo_corona.models.State;
import org.springframework.data.repository.CrudRepository;

;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface StateRepository extends CrudRepository<State, Long> {

}