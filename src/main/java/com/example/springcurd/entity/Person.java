package com.example.springcurd.entity;

import com.example.springcurd.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Person extends BaseEntity<Long> {
    String firstName;
    String lastName;
}
