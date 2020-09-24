package com.abdrakhimov.demo_corona.database;

import com.abdrakhimov.demo_corona.models.Country;

import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
}
