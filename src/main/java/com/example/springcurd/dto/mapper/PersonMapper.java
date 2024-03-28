package com.example.springcurd.dto.mapper;

import com.example.springcurd.dto.PersonDto;
import com.example.springcurd.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class PersonMapper {
    PersonDto getPersonToPersonDto(Person person);

    Person getPersonDtoToPerson(PersonDto personDto);
}
