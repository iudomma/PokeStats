package com.example.pokestats.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.pokestats.dto.PokemonInfo;
import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.dto.PokemonUri;
import com.example.pokestats.dto.PokemonsGeneral;
import com.example.pokestats.mapper.ExpMapper;
import com.example.pokestats.mapper.HeightMapper;
import com.example.pokestats.mapper.WeightMapper;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

	@Mock
	private WebClient webClient;

	@Mock
	private WeightMapper weightMapper;

	@Mock
	private HeightMapper heightMapper;

	@Mock
	private ExpMapper expMapper;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@Mock
	private Mono<PokemonsGeneral> generalMono;

	@Mock
	private Mono<PokemonInfo> pokemonMono;

	@Mock
	private PokemonsGeneral pokemonGeneral;

	@Mock
	private PokemonInfo pokemonInfo;

	@InjectMocks
	private PokemonService pokemonService;

	@BeforeEach
	void setUp() {

		WebClient.RequestHeadersSpec<?> requestSpec = mock(WebClient.RequestHeadersSpec.class);

		WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

	}

	@Test
	void shouldGetPokemonWeightSortedDescendingAndReturnTopFive() {
		ArrayList<PokemonInfo> pokemon = new ArrayList<>();

		pokemon.add(createPokemon("pokemon1", 100, 10, 10L));
		pokemon.add(createPokemon("pokemon2", 500, 20, 20L));
		pokemon.add(createPokemon("pokemon3", 300, 30, 30L));
		pokemon.add(createPokemon("pokemon4", 900, 40, 40L));
		pokemon.add(createPokemon("pokemon5", 700, 50, 50L));
		pokemon.add(createPokemon("pokemon6", 600, 60, 60L));

		PokemonResponseWeight expected = new PokemonResponseWeight();

		when(weightMapper.toPokeWeight(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseWeight result = spyService.getPokemonWeight();

		assertNotNull(result);

		verify(weightMapper).toPokeWeight(argThat((List<PokemonInfo> list) -> list.size() == 5
				&& list.get(0).getWeight() == 900 && list.get(1).getWeight() == 700 && list.get(2).getWeight() == 600
				&& list.get(3).getWeight() == 500 && list.get(4).getWeight() == 300));
	}

	@Test
	void shouldGetPokemonHeightSortedDescendingAndReturnTopFive() {
		ArrayList<PokemonInfo> pokemon = new ArrayList<>();

		pokemon.add(createPokemon("pokemon1", 100, 10, 10L));
		pokemon.add(createPokemon("pokemon2", 500, 50, 20L));
		pokemon.add(createPokemon("pokemon3", 300, 30, 30L));
		pokemon.add(createPokemon("pokemon4", 900, 90, 40L));
		pokemon.add(createPokemon("pokemon5", 700, 70, 50L));
		pokemon.add(createPokemon("pokemon6", 600, 60, 60L));

		PokemonResponseHeight expected = new PokemonResponseHeight();

		when(heightMapper.toPokeHeight(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseHeight result = spyService.getPokemonHeight();

		assertNotNull(result);

		verify(heightMapper).toPokeHeight(argThat((List<PokemonInfo> list) -> list.size() == 5
				&& list.get(0).getHeight() == 90 && list.get(1).getHeight() == 70 && list.get(2).getHeight() == 60
				&& list.get(3).getHeight() == 50 && list.get(4).getHeight() == 30));
	}

	@Test
	void shouldGetPokemonExpSortedDescendingAndReturnTopFive() {
		ArrayList<PokemonInfo> pokemon = new ArrayList<>();

		pokemon.add(createPokemon("pokemon1", 100, 10, 50L));
		pokemon.add(createPokemon("pokemon2", 500, 20, 200L));
		pokemon.add(createPokemon("pokemon3", 300, 30, 100L));
		pokemon.add(createPokemon("pokemon4", 900, 40, 400L));
		pokemon.add(createPokemon("pokemon5", 700, 50, 300L));
		pokemon.add(createPokemon("pokemon6", 600, 60, 150L));

		PokemonResponseExp expected = new PokemonResponseExp();

		when(expMapper.toPokeExp(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseExp result = spyService.getPokemonExp();

		assertNotNull(result);

		verify(expMapper).toPokeExp(
				argThat((List<PokemonInfo> list) -> list.size() == 5 && list.get(0).getBase_experience() == 400L
						&& list.get(1).getBase_experience() == 300L && list.get(2).getBase_experience() == 200L
						&& list.get(3).getBase_experience() == 150L && list.get(4).getBase_experience() == 100L));

	}

	@Test
	void shouldIgnorePokemonWithNullExperience() {
		ArrayList<PokemonInfo> pokemon = new ArrayList<>();

		pokemon.add(createPokemon("pokemon1", 100, 10, null));
		pokemon.add(createPokemon("pokemon2", 500, 20, 200L));
		pokemon.add(createPokemon("pokemon3", 300, 30, 100L));
		pokemon.add(createPokemon("pokemon4", 900, 40, 400L));
		pokemon.add(createPokemon("pokemon5", 700, 50, 300L));
		pokemon.add(createPokemon("pokemon6", 600, 60, 150L));

		PokemonResponseExp expected = new PokemonResponseExp();

		when(expMapper.toPokeExp(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseExp result = spyService.getPokemonExp();

		assertNotNull(result);

		verify(expMapper).toPokeExp(argThat((List<PokemonInfo> list) -> list.size() == 5
				&& list.stream().noneMatch(p -> p.getBase_experience() == null)));
	}

	@Test
	void shouldReturnExpectedWeightResponse() {
		ArrayList<PokemonInfo> pokemon = createSixPokemon();

		PokemonResponseWeight expected = new PokemonResponseWeight();

		when(weightMapper.toPokeWeight(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseWeight result = spyService.getPokemonWeight();

	}

	@Test
	void shouldReturnExpectedHeightResponse() {
		ArrayList<PokemonInfo> pokemon = createSixPokemon();

		PokemonResponseHeight expected = new PokemonResponseHeight();

		when(heightMapper.toPokeHeight(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseHeight result = spyService.getPokemonHeight();

	}

	@Test
	void shouldReturnExpectedExpResponse() {
		ArrayList<PokemonInfo> pokemon = createSixPokemon();

		PokemonResponseExp expected = new PokemonResponseExp();

		when(expMapper.toPokeExp(anyList())).thenReturn(Arrays.asList());

		PokemonService spyService = spy(pokemonService);

		doReturn(pokemon).when(spyService).getPokemonGeneralWebClient();

		PokemonResponseExp result = spyService.getPokemonExp();

	}

	private PokemonInfo createPokemon(String name, int weight, int height, Long experience) {

		PokemonInfo pokemon = new PokemonInfo();

		pokemon.setName(name);
		pokemon.setWeight(weight);
		pokemon.setHeight(height);
		pokemon.setBase_experience(experience);

		return pokemon;
	}

	private ArrayList<PokemonInfo> createSixPokemon() {

		ArrayList<PokemonInfo> pokemon = new ArrayList<>();

		pokemon.add(createPokemon("pokemon1", 100, 10, 10L));
		pokemon.add(createPokemon("pokemon2", 200, 20, 20L));
		pokemon.add(createPokemon("pokemon3", 300, 30, 30L));
		pokemon.add(createPokemon("pokemon4", 400, 40, 40L));
		pokemon.add(createPokemon("pokemon5", 500, 50, 50L));
		pokemon.add(createPokemon("pokemon6", 600, 60, 60L));

		return pokemon;
	}

	@Test
	void shouldFollowNextPageRecursively() {
		// Arrange

		PokemonUri firstUri = new PokemonUri();
		firstUri.setUrl("https://pokeapi.co/api/v2/pokemon/1");

		PokemonUri secondUri = new PokemonUri();
		secondUri.setUrl("https://pokeapi.co/api/v2/pokemon/2");

		PokemonsGeneral firstPage = new PokemonsGeneral();
		firstPage.setResults(new ArrayList<>(List.of(firstUri)));
		firstPage.setNext("https://pokeapi.co/api/v2/pokemon?offset=1");

		PokemonsGeneral secondPage = new PokemonsGeneral();
		secondPage.setResults(new ArrayList<>(List.of(secondUri)));
		secondPage.setNext(null);

		PokemonInfo firstPokemon = new PokemonInfo();
		firstPokemon.setName("bulbasaur");

		PokemonInfo secondPokemon = new PokemonInfo();
		secondPokemon.setName("ivysaur");

		ArrayList<PokemonInfo> pokeInfo = new ArrayList<>();

		when(webClient.get()).thenReturn(requestHeadersUriSpec);

		when(requestHeadersUriSpec.uri("https://pokeapi.co/api/v2/pokemon/1")).thenReturn(requestHeadersSpec);

		when(requestHeadersUriSpec.uri("https://pokeapi.co/api/v2/pokemon?offset=1")).thenReturn(requestHeadersSpec);

		when(requestHeadersUriSpec.uri("https://pokeapi.co/api/v2/pokemon/2")).thenReturn(requestHeadersSpec);

		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

		when(responseSpec.bodyToMono(PokemonInfo.class)).thenReturn(Mono.just(firstPokemon), Mono.just(secondPokemon));

		when(responseSpec.bodyToMono(PokemonsGeneral.class)).thenReturn(Mono.just(secondPage));

		// Act
		pokemonService.getPokemonNextWebClient(firstPage, pokeInfo);

		// Assert
		assertEquals(2, pokeInfo.size());

		assertEquals("bulbasaur", pokeInfo.get(0).getName());
		assertEquals("ivysaur", pokeInfo.get(1).getName());

		verify(requestHeadersUriSpec).uri("https://pokeapi.co/api/v2/pokemon/1");

		verify(requestHeadersUriSpec).uri("https://pokeapi.co/api/v2/pokemon?offset=1");

		verify(requestHeadersUriSpec).uri("https://pokeapi.co/api/v2/pokemon/2");

		verify(responseSpec, times(2)).bodyToMono(PokemonInfo.class);

		verify(responseSpec).bodyToMono(PokemonsGeneral.class);
	}

}