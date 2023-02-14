package com.rafaelsantos.artist.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rafaelsantos.artist.DTO.AlbumDTO;
import com.rafaelsantos.artist.entities.Album;
import com.rafaelsantos.artist.entities.Artist;
import com.rafaelsantos.artist.repositories.AlbumRepository;
import com.rafaelsantos.artist.repositories.ArtistRepository;
import com.rafaelsantos.artist.repositories.tests.Factory;
import com.rafaelsantos.artist.services.exceptions.DatabaseException;
import com.rafaelsantos.artist.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class AlbumServiceTests {

	@InjectMocks
	private AlbumService service;

	@Mock
	private AlbumRepository repository;

	@Mock
	private ArtistRepository artistRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Album> page;
	private Artist artist;
	private Album album;
	private AlbumDTO albumDto;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 2L;
		album = Factory.createAlbum();
		albumDto = Factory.createAlbumDto();
		artist = Factory.createArtist();
		page = new PageImpl<>(List.of(album));

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(album);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(album));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		Mockito.when(repository.getReferenceById(existingId)).thenReturn(album);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(artistRepository.getReferenceById(existingId)).thenReturn(artist);
		Mockito.when(artistRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, albumDto);
		});
	}

	@Test
	public void updateShouldReturnAlbumDTOWhenIdExists() {
		AlbumDTO result = service.update(existingId, albumDto);

		Assertions.assertNotNull(result);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void findByIdShouldReturnAlbumDTOWhenIdExists() {
		AlbumDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
	}

	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<AlbumDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentIdExists() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
}
