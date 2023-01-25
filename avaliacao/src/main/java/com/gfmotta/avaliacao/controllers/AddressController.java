package com.gfmotta.avaliacao.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.dtos.SimpleAddressDTO;
import com.gfmotta.avaliacao.services.AddressService;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

	@Autowired
	private AddressService service;
	
	@GetMapping(value = "/{personId}/all")
	public ResponseEntity<List<SimpleAddressDTO>> findAll(@PathVariable Long personId) {
		List<SimpleAddressDTO> dtos = service.findAll(personId);
		return ResponseEntity.ok().body(dtos);
	}
	
	@PostMapping(value = "/new")
	public ResponseEntity<AddressDTO> insert(@RequestBody AddressDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	/**Endpoint que permitirá alterar o endereço padrão da pessoa e alterar outros dados referente ao endereço*/
	@PutMapping(value = "/{addressId}")
	public ResponseEntity<AddressDTO> update(@PathVariable Long addressId, @RequestBody SimpleAddressDTO dto) {
		AddressDTO addressDto = service.update(addressId, dto);
		return ResponseEntity.ok().body(addressDto);
	}
}
