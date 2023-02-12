package com.rafaelsantos.artist.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafaelsantos.artist.DTO.ArtistDTO;
import com.rafaelsantos.artist.entities.Artist;
import com.rafaelsantos.artist.repositories.ArtistRepository;
import com.rafaelsantos.artist.services.exceptions.ResourceNotFoundException;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ArtistDTO> findAllPaged(Pageable pageable){
		Page<Artist> list = repository.findAll(pageable);
		return list.map(x -> new ArtistDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ArtistDTO findById(Long id) {
		Optional<Artist> obj = repository.findById(id);
		Artist entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ArtistDTO(entity);
	}
}
