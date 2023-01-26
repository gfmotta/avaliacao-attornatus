package com.gfmotta.avaliacao.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gfmotta.avaliacao.entities.Person;
import com.gfmotta.avaliacao.utilities.Factory;

@DataJpaTest
public class PersonRepositoryTests {

	@Autowired
	private PersonRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Person person;
	private Long total;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		person = Factory.newPerson();
		total = 3L;
	}
	
	@Test
	public void findByIdShouldReturnTheObjectWhenIdExists() {
		Optional<Person> entity = repository.findById(existingId);
		
		Assertions.assertTrue(entity.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {
		Optional<Person> entity = repository.findById(nonExistingId);
		
		Assertions.assertTrue(entity.isEmpty());
	}
	
	@Test
	public void findAllShouldReturnListOfObjects() {
		List<Person> entities = repository.findAll();
		
		Assertions.assertFalse(entities.isEmpty());
	}
	
	@Test
	public void saveShouldPersistObjectAndIncrementId() {	
		repository.save(person);
		
		Assertions.assertNotNull(person.getId());
		Assertions.assertTrue(person.getId() == total + 1);
	}
}
