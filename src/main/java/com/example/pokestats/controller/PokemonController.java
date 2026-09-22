package com.example.pokestats.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.dto.PokemonWeight;
import com.example.pokestats.service.AsyncPokemonService;
import com.example.pokestats.service.PokemonService;

@RestController
@RequestMapping(value = "/pokestats")
public class PokemonController {

	@Autowired
	private PokemonService pokemonService;
	@Autowired
	private AsyncPokemonService asyncPokemonService;

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

	@GetMapping(value = "/async")
	public ResponseEntity<PokemonResponseWeight> pokeAsync() throws InterruptedException, ExecutionException {
		CompletableFuture<PokemonResponseWeight> future1 = asyncPokemonService.findWeight(0L);
		CompletableFuture<PokemonResponseWeight> future2 = asyncPokemonService.findWeight(250L);
		CompletableFuture<PokemonResponseWeight> future3 = asyncPokemonService.findWeight(500L);
		CompletableFuture<PokemonResponseWeight> future4 = asyncPokemonService.findWeight(750L);
		CompletableFuture<PokemonResponseWeight> future5 = asyncPokemonService.findWeight(1000L);
		CompletableFuture<PokemonResponseWeight> future6 = asyncPokemonService.findWeight(1250L);

		CompletableFuture.allOf(future1, future2, future3, future4, future5, future6).join();
		PokemonResponseWeight weight = new PokemonResponseWeight();
		ArrayList<PokemonWeight> list = new ArrayList<>();

		list.addAll(future1.get().getPokemonInfo());
		list.addAll(future2.get().getPokemonInfo());
		list.addAll(future3.get().getPokemonInfo());
		list.addAll(future4.get().getPokemonInfo());
		list.addAll(future5.get().getPokemonInfo());
		list.addAll(future6.get().getPokemonInfo());

		list.sort(Comparator.comparingInt(PokemonWeight::getWeight).reversed());

		List<PokemonWeight> list2 = list.subList(0, 5);
		weight.setPokemonInfo(list2);

		return ResponseEntity.ok(weight);
	}

}
