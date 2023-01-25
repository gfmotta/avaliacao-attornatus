package com.gfmotta.avaliacao.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.entities.Person;
import com.gfmotta.avaliacao.repositories.PersonRepository;
import com.gfmotta.avaliacao.services.exceptions.ResourceNotFoundException;

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
		Person person = entity.orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
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
		try {
			Person person = repository.getReferenceById(id);
			mapper.map(dto, person);
			person = repository.save(person);
			return mapper.map(person, PersonDTO.class);
		}
		catch(MappingException e) {
			throw new ResourceNotFoundException("O registro que está tentando atualizar não existe");
		}
	}
}
