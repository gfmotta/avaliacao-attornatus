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
import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.utilities.Factory;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AddressControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long totalAddress;
	private AddressDTO addressDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		totalAddress = 3L;
		addressDto = Factory.newAddressDTO();
	}
	
	@Test
	public void findAllShouldReturnListOfAddressDtoWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/address/{personId}/all", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$").isArray());
		result.andExpect(jsonPath("$").isNotEmpty());
		result.andExpect(jsonPath("$.[0].id").value(1L));
	}
	
	@Test
	public void findAllShouldReturnEmptyListWhenIdDoesNotExist() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/address/{personId}/all", nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$").isArray());
		result.andExpect(jsonPath("$").isEmpty());
	}
	
	@Test
	public void insertShouldReturnAddressDtoWithValidId() throws Exception {	
		String json = objMapper.writeValueAsString(addressDto);
		
		ResultActions result = 
				mockMvc.perform(post("/address/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(totalAddress + 1));
		result.andExpect(jsonPath("$.area").exists());
	}
	
	@Test
	public void updateShouldReturnAddressDtoWhenIdExists() throws Exception {
		addressDto.setMainAddress(true);
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
	
	@Test
	public void updateShouldReturnBadRequestWhenInvalidAddress() throws Exception {
		String json = objMapper.writeValueAsString(addressDto);
		
		ResultActions result = 
				mockMvc.perform(put("/address/{addressId}", existingId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}
}
