package com.gfmotta.avaliacao.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.entities.Person;
import com.gfmotta.avaliacao.repositories.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private ModelMapper mapper;

	public PersonDTO findById(Long id) {
		Optional<Person> entity = repository.findById(id);
		Person person = entity.orElseThrow(() -> new RuntimeException());
		return mapper.map(person, PersonDTO.class);
	}

}
