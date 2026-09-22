package com.example.pokestats.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.pokestats.dto.PokemonExp;
import com.example.pokestats.dto.PokemonHeight;
import com.example.pokestats.dto.PokemonInfo;
import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.dto.PokemonUri;
import com.example.pokestats.dto.PokemonWeight;
import com.example.pokestats.dto.PokemonsGeneral;
import com.example.pokestats.mapper.ExpMapper;
import com.example.pokestats.mapper.HeightMapper;
import com.example.pokestats.mapper.WeightMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AsyncPokemonServiceTest {

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WeightMapper weightMapper;

	@Mock
	private HeightMapper heightMapper;

	@Mock
	private ExpMapper expMapper;

	@Spy
	@InjectMocks
	private AsyncPokemonService service;

	// ---------------------------------------------------------
	// findWeight
	// ---------------------------------------------------------

	@Test
	void findWeight_shouldSortDescendingAndReturnMaximumFive() {

		PokemonInfo pokemon1 = pokemon("Pikachu", 100, 50, 200l);
		PokemonInfo pokemon2 = pokemon("Charizard", 300, 80, 250l);
		PokemonInfo pokemon3 = pokemon("Bulbasaur", 50, 70, 100l);
		PokemonInfo pokemon4 = pokemon("Snorlax", 460, 90, 300l);
		PokemonInfo pokemon5 = pokemon("Mewtwo", 1220, 100, 350l);
		PokemonInfo pokemon6 = pokemon("Eevee", 65, 40, 150l);

		List<PokemonInfo> pokemonInfo = Arrays.asList(pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6);

		List<PokemonWeight> mappedResult = Arrays.asList(new PokemonWeight(), new PokemonWeight(), new PokemonWeight(),
				new PokemonWeight(), new PokemonWeight());

		doReturn(Mono.just(pokemonInfo)).when(service).getPokemonNextWebClientOffset(0L);

		when(weightMapper.toPokeWeight(anyList())).thenReturn(mappedResult);

		StepVerifier.create(service.findWeight(0L)).assertNext(response -> {
			assertEquals(mappedResult, response.getPokemonWeight());
		}).verifyComplete();

		ArgumentCaptor<List<PokemonInfo>> captor = ArgumentCaptor.forClass(List.class);

		verify(weightMapper).toPokeWeight(captor.capture());

		List<PokemonInfo> result = captor.getValue();

		assertEquals(5, result.size());

		// Highest weights must come first
		assertEquals(1220, result.get(0).getWeight());
		assertEquals(460, result.get(1).getWeight());
		assertEquals(300, result.get(2).getWeight());
		assertEquals(100, result.get(3).getWeight());
		assertEquals(65, result.get(4).getWeight());
	}

	@Test
	void findWeight_shouldReturnLessThanFiveWhenThereAreLessThanFivePokemon() {

		PokemonInfo pokemon1 = pokemon("Pikachu", 100, 50, 200l);
		PokemonInfo pokemon2 = pokemon("Charizard", 300, 80, 250l);

		List<PokemonInfo> pokemonInfo = Arrays.asList(pokemon1, pokemon2);

		List<PokemonWeight> mappedResult = Arrays.asList(new PokemonWeight(), new PokemonWeight());

		doReturn(Mono.just(pokemonInfo)).when(service).getPokemonNextWebClientOffset(0L);

		when(weightMapper.toPokeWeight(anyList())).thenReturn(mappedResult);

		StepVerifier.create(service.findWeight(0L))
				.assertNext(response -> assertEquals(2, response.getPokemonWeight().size())).verifyComplete();

		verify(weightMapper).toPokeWeight(anyList());
	}

	@Test
	void findExp_shouldReturnLessThanFiveWhenThereAreLessThanFivePokemon() {

		PokemonInfo pokemon1 = pokemon("Pikachu", 100, 50, 200l);
		PokemonInfo pokemon2 = pokemon("Charizard", 300, 80, 250l);

		List<PokemonInfo> pokemonInfo = Arrays.asList(pokemon1, pokemon2);

		List<PokemonExp> mappedResult = Arrays.asList(new PokemonExp(), new PokemonExp());

		doReturn(Mono.just(pokemonInfo)).when(service).getPokemonNextWebClientOffset(0L);

		when(expMapper.toPokeExp(anyList())).thenReturn(mappedResult);

		StepVerifier.create(service.findExp(0L))
				.assertNext(response -> assertEquals(2, response.getPokemonExp().size())).verifyComplete();

		verify(expMapper).toPokeExp(anyList());
	}

	// ---------------------------------------------------------
	// findHeight
	// ---------------------------------------------------------

	@Test
	void findHeight_shouldSortDescendingAndReturnMaximumFive() {

		PokemonInfo p1 = pokemon("Pikachu", 100, 50, 20l);
		PokemonInfo p2 = pokemon("Charizard", 300, 80, 250l);
		PokemonInfo p3 = pokemon("Bulbasaur", 50, 70, 100l);
		PokemonInfo p4 = pokemon("Snorlax", 460, 90, 300l);
		PokemonInfo p5 = pokemon("Mewtwo", 1220, 100, 350l);
		PokemonInfo p6 = pokemon("Eevee", 65, 40, 150l);

		doReturn(Mono.just(Arrays.asList(p1, p2, p3, p4, p5, p6))).when(service).getPokemonNextWebClientOffset(0L);

		List<PokemonHeight> mappedResult = Arrays.asList(new PokemonHeight(), new PokemonHeight(), new PokemonHeight(),
				new PokemonHeight(), new PokemonHeight());

		when(heightMapper.toPokeHeight(anyList())).thenReturn(mappedResult);

		StepVerifier.create(service.findHeight(0L))
				.assertNext(response -> assertEquals(mappedResult, response.getPokemonHeight())).verifyComplete();

		ArgumentCaptor<List<PokemonInfo>> captor = ArgumentCaptor.forClass(List.class);

		verify(heightMapper).toPokeHeight(captor.capture());

		List<PokemonInfo> result = captor.getValue();

		assertEquals(5, result.size());

		assertEquals(100, result.get(0).getHeight());
		assertEquals(90, result.get(1).getHeight());
		assertEquals(80, result.get(2).getHeight());
		assertEquals(70, result.get(3).getHeight());
		assertEquals(50, result.get(4).getHeight());
	}

	// ---------------------------------------------------------
	// getPokemonNextWebClientOffset
	// ---------------------------------------------------------

	@Test
	void getPokemonNextWebClientOffset_shouldReturnPokemonList() {

		// This test is intentionally focused on the service's reactive
		// pipeline. The WebClient chain is mocked below.

		WebClient.RequestHeadersSpec<?> requestSpec = mock(WebClient.RequestHeadersSpec.class);

		WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

		when(webClient.get()).thenReturn(requestHeadersUriSpec);

		when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestSpec);

		when(requestSpec.retrieve()).thenReturn(responseSpec);

		PokemonsGeneral general = new PokemonsGeneral();
		ArrayList<PokemonUri> listPoke = new ArrayList<>();

		// Adjust this according to the actual setter in PokemonsGeneral.
		general.setResults(listPoke);

		when(responseSpec.bodyToMono(PokemonsGeneral.class)).thenReturn(Mono.just(general));

		StepVerifier.create(service.getPokemonNextWebClientOffset(0L)).expectNext(Collections.emptyList())
				.verifyComplete();

		verify(webClient).get();
		verify(responseSpec).bodyToMono(PokemonsGeneral.class);
	}

	// ---------------------------------------------------------
	// procesWeight
	// ---------------------------------------------------------

	@Test
	void procesWeight_shouldCalculateNumberOfPagesAndReturnTopFive() {

		PokemonsGeneral general = new PokemonsGeneral();
		general.setCount(500);

		WebClient.RequestHeadersSpec<?> requestSpec = mock(WebClient.RequestHeadersSpec.class);

		WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(service.url)).thenReturn(requestSpec);
		when(requestSpec.retrieve()).thenReturn(responseSpec);

		when(responseSpec.bodyToMono(PokemonsGeneral.class)).thenReturn(Mono.just(general));

		PokemonWeight w1 = weight(100);
		PokemonWeight w2 = weight(90);
		PokemonWeight w3 = weight(80);
		PokemonWeight w4 = weight(70);
		PokemonWeight w5 = weight(60);
		PokemonWeight w6 = weight(50);

		PokemonResponseWeight response1 = new PokemonResponseWeight();
		response1.setPokemonInfo(Arrays.asList(w1, w3, w5));

		PokemonResponseWeight response2 = new PokemonResponseWeight();
		response2.setPokemonInfo(Arrays.asList(w2, w4, w6));

		doReturn(Mono.just(response1)).when(service).findWeight(0L);

		doReturn(Mono.just(response2)).when(service).findWeight(250L);

		StepVerifier.create(service.procesWeight()).assertNext(result -> {

			assertEquals(5, result.size());

			assertEquals(100, result.get(0).getWeight());
			assertEquals(90, result.get(1).getWeight());
			assertEquals(80, result.get(2).getWeight());
			assertEquals(70, result.get(3).getWeight());
			assertEquals(60, result.get(4).getWeight());
		}).verifyComplete();

		verify(service).findWeight(0L);
		verify(service).findWeight(250L);
	}

	// ---------------------------------------------------------
	// procesHeight
	// ---------------------------------------------------------

	@Test
	void procesHeight_shouldCalculatePagesAndReturnTopFive() {

		PokemonsGeneral general = new PokemonsGeneral();
		general.setCount(500);

		WebClient.RequestHeadersSpec<?> requestSpec = mock(WebClient.RequestHeadersSpec.class);

		WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(service.url)).thenReturn(requestSpec);
		when(requestSpec.retrieve()).thenReturn(responseSpec);

		when(responseSpec.bodyToMono(PokemonsGeneral.class)).thenReturn(Mono.just(general));

		PokemonHeight h1 = height(100);
		PokemonHeight h2 = height(90);
		PokemonHeight h3 = height(80);
		PokemonHeight h4 = height(70);
		PokemonHeight h5 = height(60);
		PokemonHeight h6 = height(50);

		PokemonResponseHeight response1 = new PokemonResponseHeight();
		response1.setPokemonHeight(Arrays.asList(h1, h3, h5));

		PokemonResponseHeight response2 = new PokemonResponseHeight();
		response2.setPokemonHeight(Arrays.asList(h2, h4, h6));

		doReturn(Mono.just(response1)).when(service).findHeight(0L);

		doReturn(Mono.just(response2)).when(service).findHeight(250L);

		StepVerifier.create(service.procesHeight()).assertNext(result -> {

			assertEquals(5, result.size());

			assertEquals(100, result.get(0).getHeight());
			assertEquals(90, result.get(1).getHeight());
			assertEquals(80, result.get(2).getHeight());
			assertEquals(70, result.get(3).getHeight());
			assertEquals(60, result.get(4).getHeight());
		}).verifyComplete();

		verify(service).findHeight(0L);
		verify(service).findHeight(250L);
	}

	// ---------------------------------------------------------
	// procesExp
	// ---------------------------------------------------------

	@Test
	void procesExp_shouldCalculatePagesAndReturnTopFive() {

		PokemonsGeneral general = new PokemonsGeneral();
		general.setCount(500);

		WebClient.RequestHeadersSpec<?> requestSpec = mock(WebClient.RequestHeadersSpec.class);

		WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(service.url)).thenReturn(requestSpec);
		when(requestSpec.retrieve()).thenReturn(responseSpec);

		when(responseSpec.bodyToMono(PokemonsGeneral.class)).thenReturn(Mono.just(general));

		PokemonExp e1 = exp(350l);
		PokemonExp e2 = exp(300l);
		PokemonExp e3 = exp(250l);
		PokemonExp e4 = exp(200l);
		PokemonExp e5 = exp(150l);
		PokemonExp e6 = exp(100l);

		PokemonResponseExp response1 = new PokemonResponseExp();
		response1.setPokemonExp(Arrays.asList(e1, e3, e5));

		PokemonResponseExp response2 = new PokemonResponseExp();
		response2.setPokemonExp(Arrays.asList(e2, e4, e6));

		doReturn(Mono.just(response1)).when(service).findExp(0L);

		doReturn(Mono.just(response2)).when(service).findExp(250L);

		StepVerifier.create(service.procesExp()).assertNext(result -> {

			assertEquals(5, result.size());

			assertEquals(350, result.get(0).getBase_experience());
			assertEquals(300, result.get(1).getBase_experience());
			assertEquals(250, result.get(2).getBase_experience());
			assertEquals(200, result.get(3).getBase_experience());
			assertEquals(150, result.get(4).getBase_experience());
		}).verifyComplete();

		verify(service).findExp(0L);
		verify(service).findExp(250L);
	}

	// ---------------------------------------------------------
	// Helper methods
	// ---------------------------------------------------------

	private PokemonInfo pokemon(String name, int weight, int height, Long experience) {

		PokemonInfo pokemon = new PokemonInfo();

		pokemon.setName(name);
		pokemon.setWeight(weight);
		pokemon.setHeight(height);
		pokemon.setBase_experience(experience);

		return pokemon;
	}

	private PokemonWeight weight(int value) {

		PokemonWeight pokemonWeight = new PokemonWeight();
		pokemonWeight.setWeight(value);

		return pokemonWeight;
	}

	private PokemonHeight height(int value) {

		PokemonHeight pokemonHeight = new PokemonHeight();
		pokemonHeight.setHeight(value);

		return pokemonHeight;
	}

	private PokemonExp exp(Long value) {

		PokemonExp pokemonExp = new PokemonExp();
		pokemonExp.setBase_experience(value);

		return pokemonExp;
	}
}