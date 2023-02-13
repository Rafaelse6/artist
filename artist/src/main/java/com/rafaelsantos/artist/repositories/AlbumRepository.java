package com.rafaelsantos.artist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelsantos.artist.entities.Album;

public interface AlbumRepository extends JpaRepository<Album, Long>{

}
