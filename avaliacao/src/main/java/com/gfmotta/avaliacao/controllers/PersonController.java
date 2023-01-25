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

import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.services.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

	@Autowired
	private PersonService service;
	
	@GetMapping(value = "/{personId}")
	public ResponseEntity<PersonDTO> findById(@PathVariable Long personId) {
		PersonDTO dto = service.findById(personId);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping(value = "/all")
	public ResponseEntity<List<PersonDTO>> findAll() {
		List<PersonDTO> dtos = service.findAll();
		return ResponseEntity.ok().body(dtos);
	}
	
	@PostMapping(value = "/new")
	public ResponseEntity<PersonDTO> insert(@Valid @RequestBody PersonDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{personId}")
	public ResponseEntity<PersonDTO> update(@PathVariable Long personId, @Valid @RequestBody PersonDTO dto) {
		dto = service.update(personId, dto);
		return ResponseEntity.ok().body(dto);
	}
}
