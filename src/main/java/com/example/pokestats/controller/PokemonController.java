package com.example.pokestats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.service.PokemonService;

@RestController
@RequestMapping(value = "/pokestats")
public class PokemonController {

	@Autowired
	private PokemonService pokemonService;

//	@GetMapping(value = "/weight")
//	public ResponseEntity<String> pokeWeight() {
//
//		return ResponseEntity.ok(pokemonService.getPokemonAmount());
//	}

	@GetMapping(value = "/weight")
	public ResponseEntity<PokemonResponseWeight> pokeWeight() {

		return ResponseEntity.ok(pokemonService.getPokemonWeight());
	}

	@GetMapping(value = "/height")
	public ResponseEntity<PokemonResponseHeight> pokeHeigth() {

		return ResponseEntity.ok(pokemonService.getPokemonHeight());
	}

	@GetMapping(value = "/exp")
	public ResponseEntity<PokemonResponseExp> pokeExp() {

		return ResponseEntity.ok(pokemonService.getPokemonExp());
	}

}
