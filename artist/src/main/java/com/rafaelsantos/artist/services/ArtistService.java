package com.rafaelsantos.artist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafaelsantos.artist.DTO.ArtistDTO;
import com.rafaelsantos.artist.entities.Artist;
import com.rafaelsantos.artist.repositories.ArtistRepository;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ArtistDTO> findAllPaged(Pageable pageable){
		Page<Artist> list = repository.findAll(pageable);
		return list.map(x -> new ArtistDTO(x));
	}
}
