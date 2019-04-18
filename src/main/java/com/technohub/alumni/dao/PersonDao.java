package com.technohub.alumni.dao;

import com.technohub.alumni.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<Person, Integer> {
}
