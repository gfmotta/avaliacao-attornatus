package com.gfmotta.avaliacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gfmotta.avaliacao.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
