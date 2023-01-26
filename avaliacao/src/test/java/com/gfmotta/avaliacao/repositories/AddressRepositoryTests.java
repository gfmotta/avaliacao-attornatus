package com.gfmotta.avaliacao.repositories;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gfmotta.avaliacao.entities.Address;
import com.gfmotta.avaliacao.utilities.Factory;

@DataJpaTest
public class AddressRepositoryTests {

	@Autowired
	private AddressRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Address address;
	private Long total;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		address = Factory.newAddress();
		total = 3L;
	}
	
	@Test
	public void findAllByPersonIdShouldReturnListOfObjectsWhenIdExists() {
		List<Address> entities = repository.findAllByPersonId(existingId);
		
		Assertions.assertFalse(entities.isEmpty());
		Assertions.assertTrue(entities.get(0).getId() == existingId);
	}
	
	@Test
	public void findAllByPersonIdShouldReturnEmptyListWhenIdDoesNotExist() {
		List<Address> entities = repository.findAllByPersonId(nonExistingId);
		
		Assertions.assertTrue(entities.isEmpty());
	}
	
	@Test
	public void saveShouldPersistObjectAndIncrementId() {	
		repository.save(address);
		
		Assertions.assertNotNull(address.getId());
		Assertions.assertTrue(address.getId() == total + 1);
	}
}
