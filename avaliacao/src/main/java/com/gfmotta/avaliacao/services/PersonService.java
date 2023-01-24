package com.gfmotta.avaliacao.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.entities.Person;
import com.gfmotta.avaliacao.repositories.PersonRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
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
	
	public List<PersonDTO> findAll() {
		List<Person> entities = repository.findAll();
		return entities.stream().map(x -> mapper.map(x, PersonDTO.class)).toList();
	}

	public PersonDTO insert(PersonDTO dto) {
		Person person = mapper.map(dto, Person.class);
		person = repository.save(person);
		return mapper.map(person, PersonDTO.class);
	}

	public PersonDTO update(Long id, PersonDTO dto) {
		Person entity = repository.getReferenceById(id);
		mapper.map(dto, entity);
		repository.save(entity);
		return mapper.map(entity, PersonDTO.class);
	}
}
