package com.rafaelsantos.artist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelsantos.artist.entities.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long>{

}
