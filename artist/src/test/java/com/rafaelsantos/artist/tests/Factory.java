package com.rafaelsantos.artist.tests;

import com.rafaelsantos.artist.DTO.AlbumDTO;
import com.rafaelsantos.artist.entities.Album;
import com.rafaelsantos.artist.entities.Artist;

public class Factory {
	
	public static Album createAlbum() {
		Album album = new Album(1L, "Scenes From A Memory", "https://img.com/img.png");
		album.getArtist().add(createArtist());
		return album;
	}
	
	public static AlbumDTO createAlbumDto() {
		Album album = createAlbum();
		return new AlbumDTO(album, album.getArtist());
	}
	
	public static Artist createArtist() {
		return new Artist(1L, "Dream Theater", "USA");
	}
}
