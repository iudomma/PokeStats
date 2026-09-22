package com.example.pokestats.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pokestats.dto.PokemonExp;
import com.example.pokestats.dto.PokemonHeight;
import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.dto.PokemonWeight;
import com.example.pokestats.service.AsyncPokemonService;
import com.example.pokestats.service.PokemonService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/pokestats")
public class PokemonController {

	@Autowired
	private PokemonService pokemonService;
	@Autowired
	private AsyncPokemonService asyncPokemonService;

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

	@GetMapping(value = "/asyncWeight")
	public Mono<List<PokemonWeight>> pokeAsyncWeight() throws InterruptedException, ExecutionException {

		return asyncPokemonService.procesWeight();
	}

	@GetMapping(value = "/asyncHeight")
	public Mono<List<PokemonHeight>> pokeAsyncHeight() throws InterruptedException, ExecutionException {

		return asyncPokemonService.procesHeight();
	}

	@GetMapping(value = "/asyncExp")
	public Mono<List<PokemonExp>> pokeAsyncExp() throws InterruptedException, ExecutionException {

		return asyncPokemonService.procesExp();
	}

}
