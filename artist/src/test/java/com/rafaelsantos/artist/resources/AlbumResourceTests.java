package com.rafaelsantos.artist.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelsantos.artist.DTO.AlbumDTO;
import com.rafaelsantos.artist.services.AlbumService;
import com.rafaelsantos.artist.services.exceptions.DatabaseException;
import com.rafaelsantos.artist.services.exceptions.ResourceNotFoundException;
import com.rafaelsantos.artist.tests.Factory;

@WebMvcTest(AlbumResource.class)
public class AlbumResourceTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private AlbumService service;
	
	private AlbumDTO albumDto;
	
	private PageImpl<AlbumDTO> page;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 2L;
		
		albumDto = Factory.createAlbumDto();
		page = new PageImpl<>(List.of(albumDto));
		
		Mockito.when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findById(existingId)).thenReturn(albumDto);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.update(eq(existingId), any())).thenReturn(albumDto);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(albumDto);
	
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}
	
	@Test
	public void insertShouldReturnCreatedAndProductDTO() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(albumDto);

		ResultActions result = 
				mockMvc.perform(post("/albums")
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));


		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception{
		ResultActions result = 
				mockMvc.perform(delete("/albums/{id}", existingId)
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}


	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = 
				mockMvc.perform(delete("/albums/{id}", nonExistingId)
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnAlbumDTOWhenIdExists() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(albumDto);

		ResultActions result = 
				mockMvc.perform(put("/albums/{id}", existingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));


		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
	}

	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

		String jsonBody = objectMapper.writeValueAsString(albumDto);
		
		ResultActions result = 
				mockMvc.perform(put("/albums/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnAlbumWhenIdExist() throws Exception{
		ResultActions result = 
				mockMvc.perform(get("/albums/{id}", existingId)
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		ResultActions result = 
				mockMvc.perform(get("/albums/{id}", nonExistingId)
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void findAllShouldReturnAPage() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/albums")
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}
}
