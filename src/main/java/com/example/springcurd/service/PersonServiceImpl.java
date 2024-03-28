package com.example.springcurd.service;

import com.example.springcurd.customException.CustomRuntimeException;
import com.example.springcurd.entity.Person;
import com.example.springcurd.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    protected PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(rollbackFor = CustomRuntimeException.class)
    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional(rollbackFor = CustomRuntimeException.class)
    @Override
    public Person update(Long id, Person person) {
        Optional<Person> foundPerson = personRepository.findById(id);
        if (foundPerson.isPresent()) {
            Person exsitingPerson = foundPerson.get();
            exsitingPerson.setFirstName(person.getFirstName());
            exsitingPerson.setLastName(person.getLastName());
            return personRepository.save(exsitingPerson);
        } else {
            throw new CustomRuntimeException("Person not found");
        }
    }

    @Transactional(rollbackFor = CustomRuntimeException.class)
    @Override
    public Person partialUpdate(Long id, Person person) {
        return personRepository.findById(id).map(existingPerson -> {
            Optional.ofNullable(person.getFirstName()).ifPresent(existingPerson::setFirstName);
            Optional.ofNullable(person.getLastName()).ifPresent(existingPerson::setLastName);
            return personRepository.save(existingPerson);
        }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void removeById(Long id) {
        personRepository.findById(id).ifPresentOrElse(
                person -> personRepository.deleteById(id),
                () -> {
                    throw new CustomRuntimeException("person not found");
                }
        );
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(personRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public List<Person> findAll() {
        List<Person> personList = personRepository.findAll();
        if (personList.isEmpty()) {
            throw new RuntimeException();
        }
        return personList;
    }

    @Override
    public List<Person> findByFirstName(String firstName) {
        List<Person> personList = personRepository.findByFirstName(firstName);
        if (personList.isEmpty()) {
            throw new EntityNotFoundException("No persons found with the given first name");
        }
        return personList;
    }

    @Override
    public Page<Person> findByLastName(String lastName, Pageable pageable) {
        Page<Person> personPage = personRepository.findByLastName(lastName, pageable);
        if (personPage.isEmpty()) {
            throw new EntityNotFoundException("No persons found with the given last name");
        }
        return personPage;
    }
}
