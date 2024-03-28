package com.example.springcurd.controller;

import com.example.springcurd.customException.CustomRuntimeException;
import com.example.springcurd.dto.PersonDto;
import com.example.springcurd.dto.mapper.PersonMapper;
import com.example.springcurd.entity.Person;
import com.example.springcurd.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/person")
@Slf4j
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @PostMapping("/save")
    public ResponseEntity<PersonDto> save(@Valid @RequestBody PersonDto personDto, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        Person person = personMapper.getPersonDtoToPerson(personDto);
        Person savedPerson = personService.save(person);
        log.info("Saving :: {}", savedPerson.getLastName());
        return new ResponseEntity<>(personMapper.getPersonToPersonDto(savedPerson), HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person personDetails, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        return new ResponseEntity<>(personService.update(id, personDetails), HttpStatus.OK);
    }

    @PatchMapping("/partialUpdate/{id}")
    public ResponseEntity<PersonDto> partialUpdate(@PathVariable Long id, @Valid @RequestBody Person personDetails, BindingResult bindingResult) {
        handleValidationErrors(bindingResult);
        Person person = personService.partialUpdate(id, personDetails);
        PersonDto personDto = personMapper.getPersonToPersonDto(person);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @DeleteMapping("/removeById/{id}")
    public ResponseEntity<String> removeById(@PathVariable Long id) {
        personService.removeById(id);
        return ResponseEntity.ok("deleted successfully");
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable Long id) {
        Optional<Person> foundPerson = personService.findById(id);
        return foundPerson.map(person -> {
            PersonDto personDto = personMapper.getPersonToPersonDto(person);
            return new ResponseEntity<>(personDto, HttpStatus.FOUND);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PersonDto>> findAll() {
        List<Person> personList = personService.findAll();
        List<PersonDto> personDtoList = personList.stream().map(personMapper::getPersonToPersonDto).toList();
        return new ResponseEntity<>(personDtoList, HttpStatus.FOUND);
    }

    @GetMapping("/findByFirstName")
    public ResponseEntity<List<PersonDto>> findByFirstName(@RequestParam String firstName) {
        List<Person> personList = personService.findByFirstName(firstName);
        List<PersonDto> personDtoList = personList.stream().map(personMapper::getPersonToPersonDto).toList();
        return new ResponseEntity<>(personDtoList, HttpStatus.FOUND);
    }

    @GetMapping("/findByLastName")
    public ResponseEntity<Page<PersonDto>> findByLastName(@RequestParam String lastName
            , @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable) {
        Page<Person> personPage = personService.findByLastName(lastName, pageable);
        Page<PersonDto> personDto = personPage.map(personMapper::getPersonToPersonDto);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    private void handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            HashMap<String, String> validationError = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                validationError.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomRuntimeException(String.valueOf(validationError));
        }
    }
}
