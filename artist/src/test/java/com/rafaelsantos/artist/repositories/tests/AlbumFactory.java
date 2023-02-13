package com.rafaelsantos.artist.repositories.tests;

import com.rafaelsantos.artist.DTO.AlbumDTO;
import com.rafaelsantos.artist.entities.Album;
import com.rafaelsantos.artist.entities.Artist;

public class AlbumFactory {
	
	public static Album createAlbum() {
		Album album = new Album(1L, "Scenes From A Memory", "https://img.com/img.png");
		album.getArtist().add(new Artist(2L, "Dream Theater", "USA"));
		return album;
	}
	
	public static AlbumDTO createAlbumDto() {
		Album album = createAlbum();
		return new AlbumDTO(album, album.getArtist());
	}
}
