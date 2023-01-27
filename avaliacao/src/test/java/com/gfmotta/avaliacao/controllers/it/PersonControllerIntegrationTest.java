package com.gfmotta.avaliacao.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.utilities.Factory;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PersonControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long totalPerson;
	private PersonDTO personDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		totalPerson = 3L;
		personDto = Factory.newPersonDTO();
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
		result.andExpect(jsonPath("$").isNotEmpty());
		result.andExpect(jsonPath("$.[0].id").value(1L));
	}
	
	@Test
	public void insertShouldReturnPersonDto() throws Exception {
		String expectedName = personDto.getName();
		String json = objMapper.writeValueAsString(personDto);
		
		ResultActions result = 
				mockMvc.perform(post("/person/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(totalPerson + 1));
		result.andExpect(jsonPath("$.name").value(expectedName));
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
