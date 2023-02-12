package com.rafaelsantos.artist.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafaelsantos.artist.DTO.ArtistDTO;
import com.rafaelsantos.artist.services.ArtistService;

@RestController
@RequestMapping(value = "/artists")
public class ArtistResource {
	
	@Autowired
	private ArtistService service;
	
	@GetMapping
	public ResponseEntity<Page<ArtistDTO>> findAll(Pageable pageable){
		Page<ArtistDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ArtistDTO> findById(@PathVariable Long id){
		ArtistDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
}
