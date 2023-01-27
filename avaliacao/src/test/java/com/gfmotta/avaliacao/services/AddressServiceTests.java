package com.gfmotta.avaliacao.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.dtos.SimpleAddressDTO;
import com.gfmotta.avaliacao.entities.Address;
import com.gfmotta.avaliacao.repositories.AddressRepository;
import com.gfmotta.avaliacao.services.exceptions.DatabaseException;
import com.gfmotta.avaliacao.services.exceptions.ResourceNotFoundException;
import com.gfmotta.avaliacao.utilities.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class AddressServiceTests {

	@InjectMocks
	private AddressService service;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private AddressRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private Address address;
	private AddressDTO addressDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		address = Factory.newAddress();
		addressDto = Factory.newAddressDTO();
		
		when(mapper.map(any(Address.class), any())).thenReturn(addressDto);
		when(mapper.map(any(AddressDTO.class), any())).thenReturn(address);
		
		when(repository.findAllByPersonId(existingId)).thenReturn(List.of(address));
		when(repository.findAllByPersonId(nonExistingId)).thenReturn(List.of());
		when(repository.save(any())).thenReturn(address);
		when(repository.getReferenceById(existingId)).thenReturn(address);
		when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
	@Test
	public void findAllShouldReturnListOfSimpleAddressDto() {
		List<SimpleAddressDTO> dtos = service.findAll(existingId);
		
		Assertions.assertFalse(dtos.isEmpty());
		verify(repository).findAllByPersonId(existingId);
	}
	
	@Test
	public void findAllShouldReturnEmptyListWhenIdDoesNotExist() {
		List<SimpleAddressDTO> dtos = service.findAll(nonExistingId);
		
		Assertions.assertTrue(dtos.isEmpty());
		verify(repository).findAllByPersonId(nonExistingId);
	}
	
	@Test
	public void insertShouldReturnAddressDto() {
		AddressDTO dto = service.insert(addressDto);
		
		Assertions.assertNotNull(dto);
		verify(repository).save(any());
	}
	
	@Test
	public void updateShouldReturnAddressDtoWhenIdExists() {
		AddressDTO dto = service.update(existingId, addressDto);
		
		Assertions.assertNotNull(dto);
		verify(repository).getReferenceById(existingId);
		verify(repository).save(any());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, addressDto);
		});

		verify(repository).getReferenceById(nonExistingId);
	}
	
	@Test
	public void updateShouldThrowDatabaseExceptionWhenInvalidAddress() {
		address.setMainAddress(true);
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.update(existingId, addressDto);
		});

		verify(repository).getReferenceById(existingId);
	}
}
