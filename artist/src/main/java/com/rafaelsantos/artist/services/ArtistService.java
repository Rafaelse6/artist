package com.rafaelsantos.artist.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafaelsantos.artist.DTO.ArtistDTO;
import com.rafaelsantos.artist.entities.Artist;
import com.rafaelsantos.artist.repositories.ArtistRepository;
import com.rafaelsantos.artist.services.exceptions.DatabaseException;
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
	
	@Transactional(readOnly = true)
	public ArtistDTO insert(ArtistDTO dto) {
		Artist entity = new Artist();
		entity.setName(dto.getName());
		entity.setCountry(dto.getCountry());
		entity = repository.save(entity);
		
		return new ArtistDTO(entity);
	}
	
	@Transactional
	public ArtistDTO update(Long id, ArtistDTO dto) {
		try {
			Artist entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity.setCountry(dto.getCountry());
			entity = repository.save(entity);
			return new ArtistDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}

	}
}
