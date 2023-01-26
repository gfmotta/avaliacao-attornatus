package com.gfmotta.avaliacao.services;

import java.util.List;

import org.modelmapper.MappingException;
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

	/**
	 * Para a criação de um endereço estou considerando como uma regra de negócio que o primeiro endereço
	 * que a pessoa cadastrar deve ser considerado como o principal, assim será garantido que toda pessoa
	 * terá um endereço principal. Caso a pessoa queira definir outro endereço,
	 * poderá fazer pelo endpoint de atualização(update) ou durante o cadastro do novo endereço.
	 */
	public AddressDTO insert(AddressDTO dto) {
		validateAddress(dto);
		Address entity = mapper.map(dto, Address.class);
		entity = repository.save(entity);
		return mapper.map(entity, AddressDTO.class);
	}

	public AddressDTO update(Long addressId, SimpleAddressDTO dto) {
		try {
			Address entity = repository.getReferenceById(addressId);
		
			//Primeiro if verifica se há uma tentativa de definir um novo endereço principal
			//Segundo if verifica se a pessoa está tentando desmarcar o endereço principal sem informar um novo
			if (dto.isMainAddress()) {
				changeMainAddress(entity.getPerson().getId());
			}
			else if (entity.isMainAddress()) {
				throw new DatabaseException("Operação negada! É necessario ter um endereço principal definido");
			}
			
			mapper.map(dto, entity);
			entity = repository.save(entity);
			return mapper.map(entity, AddressDTO.class);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("O registro que está tentando atualizar não existe");
		}
		catch(MappingException e) {
			throw new ResourceNotFoundException("O registro que está tentando atualizar não existe");
		}
	}
	
	/**
	 * Método para garantir que a pessoa tenha pelo menos um e somente um endereço principal.
	 */
	private void validateAddress(AddressDTO dto) {
		List<Address> list = repository.findAllByPersonId(dto.getPersonId());
		if (list.isEmpty()) {
			dto.setMainAddress(true);
		}
		else if (dto.isMainAddress()) {
			changeMainAddress(dto.getPersonId());
		}
	}
	
	/**
	 * Método usado para desmarcar o endereço principal antigo, 
	 * garantindo assim que o novo endereço possa ser cadastrado 
	 * sem quebrar a regra de endereço principal único.
	 */
	private void changeMainAddress(Long personId) {
		Address entity = repository.findMainAddress(personId);
		entity.setMainAddress(false);
		repository.save(entity);
	}	
}
