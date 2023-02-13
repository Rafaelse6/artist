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

import com.rafaelsantos.artist.DTO.AlbumDTO;
import com.rafaelsantos.artist.DTO.ArtistDTO;
import com.rafaelsantos.artist.entities.Album;
import com.rafaelsantos.artist.entities.Artist;
import com.rafaelsantos.artist.repositories.AlbumRepository;
import com.rafaelsantos.artist.repositories.ArtistRepository;
import com.rafaelsantos.artist.services.exceptions.DatabaseException;
import com.rafaelsantos.artist.services.exceptions.ResourceNotFoundException;

@Service
public class AlbumService {

	@Autowired
	private AlbumRepository repository;

	@Autowired
	private ArtistRepository artistRepository;

	@Transactional(readOnly = true)
	public Page<AlbumDTO> findAllPaged(Pageable pageable) {
		Page<Album> list = repository.findAll(pageable);
		return list.map(x -> new AlbumDTO(x));
	}

	@Transactional(readOnly = true)
	public AlbumDTO findById(Long id) {
		Optional<Album> obj = repository.findById(id);
		Album entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new AlbumDTO(entity, entity.getArtist());
	}

	@Transactional(readOnly = true)
	public AlbumDTO insert(AlbumDTO dto) {
		Album entity = new Album();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);

		return new AlbumDTO(entity);
	}

	@Transactional
	public AlbumDTO update(Long id, AlbumDTO dto) {
		try {
			Album entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);

			return new AlbumDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(AlbumDTO dto, Album entity) {
		entity.setName(dto.getName());
		entity.setImgUrl(dto.getImgUrl());

		entity.getArtist().clear();

		for (ArtistDTO artDto : dto.getArtist()) {
			Artist artist = artistRepository.getReferenceById(artDto.getId());
			entity.getArtist().add(artist);
		}
	}
}
