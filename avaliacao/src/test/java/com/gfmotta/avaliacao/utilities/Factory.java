package com.gfmotta.avaliacao.utilities;

import java.time.LocalDate;

import com.gfmotta.avaliacao.entities.Address;
import com.gfmotta.avaliacao.entities.Person;

public class Factory {
	
	public static Person newPerson() {
		Person person = new Person(null, "Luciano Souza", LocalDate.parse("1990-01-01"));
		return person;
	}
	
	public static Address newAddress() {
		Person person = newPerson();
		Address address = new Address(null, "Avenida Albert", "17800", 567, "Presidente Prudente", false, person);
		return address;
	}
}
