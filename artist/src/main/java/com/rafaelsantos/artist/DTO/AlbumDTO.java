package com.rafaelsantos.artist.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.rafaelsantos.artist.entities.Album;
import com.rafaelsantos.artist.entities.Artist;

public class AlbumDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String imgUrl;
	
	private List<ArtistDTO> artist = new ArrayList<>();
	
	public AlbumDTO() {
		
	}

	public AlbumDTO(Long id, String name, String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
	}
	
	public AlbumDTO(Album entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.imgUrl = entity.getImgUrl();
	}
	
	public AlbumDTO(Album entity, Set<Artist> artist) {
		this(entity);
		artist.forEach(cat -> this.artist.add(new ArtistDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<ArtistDTO> getArtist() {
		return artist;
	}
}
