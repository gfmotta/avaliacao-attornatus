package com.gfmotta.avaliacao.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.entities.Person;
import com.gfmotta.avaliacao.repositories.PersonRepository;
import com.gfmotta.avaliacao.services.exceptions.ResourceNotFoundException;
import com.gfmotta.avaliacao.utilities.Factory;

@ExtendWith(SpringExtension.class)
public class PersonServiceTests {

	@InjectMocks
	private PersonService service;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private PersonRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private Person person;
	private PersonDTO personDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		person = Factory.newPerson();
		personDto = Factory.newPersonDTO();

		when(mapper.map(any(Person.class), any())).thenReturn(personDto);
		when(mapper.map(any(PersonDTO.class), any())).thenReturn(person);
		
		when(repository.findById(existingId)).thenReturn(Optional.of(person));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		when(repository.findAll()).thenReturn(List.of(person));
		when(repository.save(any())).thenReturn(person);
		when(repository.getReferenceById(existingId)).thenReturn(person);
		when(repository.getReferenceById(nonExistingId)).thenThrow(MappingException.class);
	}
	
	@Test
	public void findByIdShouldReturnPersonDtoWhenIdExists() {
		PersonDTO dto = service.findById(existingId);
		
		Assertions.assertNotNull(dto);
		verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});

		verify(repository).findById(nonExistingId);
	}
	
	@Test
	public void findAllShouldReturnListOfPersonDto() {
		List<PersonDTO> dtos = service.findAll();
		
		Assertions.assertFalse(dtos.isEmpty());
		verify(repository).findAll();
	}
	
	@Test
	public void insertShouldReturnPersonDto() {
		PersonDTO dto = service.insert(personDto);
		
		Assertions.assertNotNull(dto);
		verify(repository).save(any());
	}
	
	@Test
	public void updateShouldReturnPersonDtoWhenIdExists() {
		PersonDTO dto = service.update(existingId, personDto);
		
		Assertions.assertNotNull(dto);
		verify(repository).getReferenceById(existingId);
		verify(repository).save(any());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, personDto);
		});

		verify(repository).getReferenceById(nonExistingId);
	}
}
