package com.gfmotta.avaliacao.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.dtos.SimpleAddressDTO;
import com.gfmotta.avaliacao.entities.Address;
import com.gfmotta.avaliacao.repositories.AddressRepository;
import com.gfmotta.avaliacao.services.exceptions.DatabaseException;
import com.gfmotta.avaliacao.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {
	
	@Autowired
	private AddressRepository repository;
	
	@Autowired
	private ModelMapper mapper;

	public List<SimpleAddressDTO> findAll(Long personId) {
		List<Address> entities = repository.findAllByPersonId(personId);
		return entities.stream().map(x -> mapper.map(x, SimpleAddressDTO.class)).toList();
	}

	public AddressDTO insert(AddressDTO dto) {
		try {
			if (dto.isMainAddress()) {
				changeMainAddress(dto.getPersonId());
			}
			Address address = mapper.map(dto, Address.class);
			address = repository.save(address);
			return mapper.map(address, AddressDTO.class);
		} 
		catch(NullPointerException e) {
			throw new DatabaseException("O campo MainAddres não pode ser nulo");
		}
	}

	public AddressDTO update(Long addressId, SimpleAddressDTO dto) {
		try {
			Address address = repository.getReferenceById(addressId);
			
			/**Caso dto seja verdadeiro e address falso, significa que o endereço principal esta sendo alterado,
			 * Caso contrario não há necessidade de chamar o metodo changeMainAddress*/
			if (dto.isMainAddress() && !address.isMainAddress()) {
				changeMainAddress(address.getPerson().getId());
			}
			
			mapper.map(dto, address);
			address = repository.save(address);
			return mapper.map(address, AddressDTO.class);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("O registro que está tentando atualizar não existe");
		}
	}
	
	/**Metodo para marcar o antigo endereço principal da pessoa como falso,
	 * para que haja apenas um unico endereço principal para a pessoa*/
	private void changeMainAddress(Long personId) {
		Address entity = repository.findMainAddress(personId);
		if (entity != null) {
			entity.setMainAddress(false);
			repository.save(entity);
		}
	}	
}
