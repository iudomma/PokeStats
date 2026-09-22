
package com.example.pokestats.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.pokestats.dto.PokemonExp;
import com.example.pokestats.dto.PokemonHeight;
import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.dto.PokemonWeight;
import com.example.pokestats.service.AsyncPokemonService;
import com.example.pokestats.service.PokemonService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

	@Mock
	private PokemonService pokemonService;

	@Mock
	private AsyncPokemonService asyncPokemonService;

	@InjectMocks
	private PokemonController pokemonController;

	private PokemonResponseWeight weightResponse;
	private PokemonResponseHeight heightResponse;
	private PokemonResponseExp expResponse;

	private List<PokemonWeight> weightList;
	private List<PokemonHeight> heightList;
	private List<PokemonExp> expList;

	@BeforeEach
	void setUp() {
		weightResponse = new PokemonResponseWeight();
		heightResponse = new PokemonResponseHeight();
		expResponse = new PokemonResponseExp();

		weightList = Arrays.asList(new PokemonWeight());
		heightList = Arrays.asList(new PokemonHeight());
		expList = Arrays.asList(new PokemonExp());
	}

	@Test
	void shouldReturnPokemonWeight() {

		// Given
		when(pokemonService.getPokemonWeight()).thenReturn(weightResponse);

		// When
		ResponseEntity<PokemonResponseWeight> response = pokemonController.pokeWeight();

		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertSame(weightResponse, response.getBody());

		verify(pokemonService).getPokemonWeight();
	}

	@Test
	void shouldReturnPokemonHeight() {

		// Given
		when(pokemonService.getPokemonHeight()).thenReturn(heightResponse);

		// When
		ResponseEntity<PokemonResponseHeight> response = pokemonController.pokeHeigth();

		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertSame(heightResponse, response.getBody());

		verify(pokemonService).getPokemonHeight();
	}

	@Test
	void shouldReturnPokemonExp() {

		// Given
		when(pokemonService.getPokemonExp()).thenReturn(expResponse);

		// When
		ResponseEntity<PokemonResponseExp> response = pokemonController.pokeExp();

		// Then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertSame(expResponse, response.getBody());

		verify(pokemonService).getPokemonExp();
	}

	@Test
	void shouldReturnAsyncPokemonWeight() throws InterruptedException, ExecutionException {

		// Given
		Mono<List<PokemonWeight>> expectedResponse = Mono.just(weightList);

		when(asyncPokemonService.procesWeight()).thenReturn(expectedResponse);

		// When
		Mono<List<PokemonWeight>> response = pokemonController.pokeAsyncWeight();

		// Then
		assertNotNull(response);
		assertSame(expectedResponse, response);

		verify(asyncPokemonService).procesWeight();
	}

	@Test
	void shouldReturnAsyncPokemonHeight() throws InterruptedException, ExecutionException {

		// Given
		Mono<List<PokemonHeight>> expectedResponse = Mono.just(heightList);

		when(asyncPokemonService.procesHeight()).thenReturn(expectedResponse);

		// When
		Mono<List<PokemonHeight>> response = pokemonController.pokeAsyncHeight();

		// Then
		assertNotNull(response);
		assertSame(expectedResponse, response);

		verify(asyncPokemonService).procesHeight();
	}

	@Test
	void shouldReturnAsyncPokemonExp() throws InterruptedException, ExecutionException {

		// Given
		Mono<List<PokemonExp>> expectedResponse = Mono.just(expList);

		when(asyncPokemonService.procesExp()).thenReturn(expectedResponse);

		// When
		Mono<List<PokemonExp>> response = pokemonController.pokeAsyncExp();

		// Then
		assertNotNull(response);
		assertSame(expectedResponse, response);

		verify(asyncPokemonService).procesExp();
	}
}