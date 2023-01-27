package com.gfmotta.avaliacao.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.services.PersonService;
import com.gfmotta.avaliacao.services.exceptions.ResourceNotFoundException;
import com.gfmotta.avaliacao.utilities.Factory;

@WebMvcTest(PersonController.class)
public class PersonControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objMapper;
	
	@MockBean
	private PersonService service;
	
	private long existingId;
	private long nonExistingId;
	private PersonDTO personDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		personDto = Factory.newPersonDTO();
		
		when(service.findById(existingId)).thenReturn(personDto);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		when(service.findAll()).thenReturn(List.of(personDto));
		when(service.insert(any())).thenReturn(personDto);
		when(service.update(eq(existingId), any())).thenReturn(personDto);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
	}

	@Test
	public void findByIdShouldReturnPersonDtoWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/person/{personId}", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/person/{personId}", nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllShouldReturnListOfPersonDto() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/person/all")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	public void insertShouldReturnPersonDto() throws Exception {	
		String json = objMapper.writeValueAsString(personDto);
		
		ResultActions result = 
				mockMvc.perform(post("/person/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void updateShouldReturnPersonDtoWhenIdExists() throws Exception {
		String json = objMapper.writeValueAsString(personDto);
		
		ResultActions result = 
				mockMvc.perform(put("/person/{personId}", existingId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {	
		String json = objMapper.writeValueAsString(personDto);
		
		ResultActions result = 
				mockMvc.perform(put("/person/{personId}", nonExistingId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}
