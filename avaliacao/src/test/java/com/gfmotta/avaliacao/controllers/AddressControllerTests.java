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
import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.services.AddressService;
import com.gfmotta.avaliacao.services.exceptions.ResourceNotFoundException;
import com.gfmotta.avaliacao.utilities.Factory;

@WebMvcTest(AddressController.class)
public class AddressControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objMapper;
	
	@MockBean
	private AddressService service;
	
	private long existingId;
	private long nonExistingId;
	private AddressDTO addressDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		addressDto = Factory.newAddressDTO();
		
		when(service.findAll(existingId)).thenReturn(List.of(addressDto));
		when(service.insert(any())).thenReturn(addressDto);
		when(service.update(eq(existingId), any())).thenReturn(addressDto);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
	}

	@Test
	public void findAllShouldReturnListOfAddressDto() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/address/{personId}/all", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$").isArray());
		result.andExpect(jsonPath("$.[0].id").value(existingId));
	}
	
	@Test
	public void insertShouldReturnAddressDto() throws Exception {	
		String json = objMapper.writeValueAsString(addressDto);
		
		ResultActions result = 
				mockMvc.perform(post("/address/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.area").exists());
	}
	
	@Test
	public void updateShouldReturnAddressDtoWhenIdExists() throws Exception {
		String json = objMapper.writeValueAsString(addressDto);
		
		ResultActions result = 
				mockMvc.perform(put("/address/{addressId}", existingId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.area").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {	
		String json = objMapper.writeValueAsString(addressDto);
		
		ResultActions result = 
				mockMvc.perform(put("/address/{addressId}", nonExistingId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}
