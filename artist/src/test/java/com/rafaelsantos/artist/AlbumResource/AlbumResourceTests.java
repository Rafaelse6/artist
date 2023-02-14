package com.rafaelsantos.artist.AlbumResource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.rafaelsantos.artist.DTO.AlbumDTO;
import com.rafaelsantos.artist.repositories.tests.Factory;
import com.rafaelsantos.artist.resources.AlbumResource;
import com.rafaelsantos.artist.services.AlbumService;
import com.rafaelsantos.artist.services.exceptions.ResourceNotFoundException;

@WebMvcTest(AlbumResource.class)
public class AlbumResourceTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AlbumService service;
	
	private AlbumDTO albumDto;
	
	private PageImpl<AlbumDTO> page;
	
	private Long existingId;
	private Long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 2L;
		
		albumDto = Factory.createAlbumDto();
		page = new PageImpl<>(List.of(albumDto));
		
		Mockito.when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findById(existingId)).thenReturn(albumDto);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
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
