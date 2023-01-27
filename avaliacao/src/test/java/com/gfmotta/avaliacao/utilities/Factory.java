package com.gfmotta.avaliacao.utilities;

import java.time.LocalDate;

import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.entities.Address;
import com.gfmotta.avaliacao.entities.Person;

public class Factory {
	
	public static Person newPerson() {
		Person person = new Person(null, "Luciano Souza", LocalDate.parse("1990-01-01"));
		return person;
	}
	
	public static PersonDTO newPersonDTO() {
		PersonDTO dto = new PersonDTO(1L, "Luciano Souza", LocalDate.parse("1990-01-01"));
		return dto;
	}
	
	public static Address newAddress() {
		Person person = newPerson();
		Address address = new Address(null, "Avenida Albert", "17800", 567, "Presidente Prudente", false, person);
		return address;
	}
	
	public static AddressDTO newAddressDTO() {
		AddressDTO dto = new AddressDTO(1L, "Avenida Albert", "17800", 567, "Presidente Prudente", false, 1L);
		return dto;
	}
}
