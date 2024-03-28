package com.example.springcurd.service;

import com.example.springcurd.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Person save(Person person);

    Person update(Long id, Person person);

    Person partialUpdate(Long id, Person person);

    void removeById(Long id);

    Optional<Person> findById(Long id);

    List<Person> findAll();

    List<Person> findByFirstName(String firstName);

    Page<Person> findByLastName(String lastName, Pageable pageable);
}
