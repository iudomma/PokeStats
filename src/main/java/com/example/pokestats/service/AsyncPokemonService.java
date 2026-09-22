package com.example.pokestats.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.pokestats.dto.PokemonExp;
import com.example.pokestats.dto.PokemonHeight;
import com.example.pokestats.dto.PokemonInfo;
import com.example.pokestats.dto.PokemonResponseExp;
import com.example.pokestats.dto.PokemonResponseHeight;
import com.example.pokestats.dto.PokemonResponseWeight;
import com.example.pokestats.dto.PokemonWeight;
import com.example.pokestats.dto.PokemonsGeneral;
import com.example.pokestats.mapper.ExpMapper;
import com.example.pokestats.mapper.HeightMapper;
import com.example.pokestats.mapper.WeightMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AsyncPokemonService {

	@Autowired
	private WebClient webClient;
	@Autowired
	private WeightMapper weightMapper;
	@Autowired
	private HeightMapper heightMapper;
	@Autowired
	private ExpMapper expMapper;

	String url = "https://pokeapi.co/api/v2/pokemon/";

	public Mono<PokemonResponseWeight> findWeight(Long offset) {

		return getPokemonNextWebClientOffset(offset).map(pokeInfo -> {

			pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getWeight).reversed());

			List<PokemonInfo> list = pokeInfo.subList(0, Math.min(5, pokeInfo.size()));

			PokemonResponseWeight rsp = new PokemonResponseWeight();

			rsp.setPokemonInfo(weightMapper.toPokeWeight(list));

			return rsp;
		});
	}

	public Mono<PokemonResponseHeight> findHeight(Long offset) {

		return getPokemonNextWebClientOffset(offset).map(pokeInfo -> {

			pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getHeight).reversed());

			List<PokemonInfo> list = pokeInfo.subList(0, Math.min(5, pokeInfo.size()));

			PokemonResponseHeight rsp = new PokemonResponseHeight();

			rsp.setPokemonHeight(heightMapper.toPokeHeight(list));

			return rsp;
		});
	}

	public Mono<PokemonResponseExp> findExp(Long offset) {

		return getPokemonNextWebClientOffset(offset).map(pokeInfo -> {
			pokeInfo.removeIf(o -> o.getBase_experience() == null);
			pokeInfo.sort(Comparator.comparing(PokemonInfo::getBase_experience).reversed());
			List<PokemonInfo> list = pokeInfo.subList(0, Math.min(5, pokeInfo.size()));

			PokemonResponseExp rsp = new PokemonResponseExp();

			rsp.setPokemonExp(expMapper.toPokeExp(list));

			return rsp;
		});
	}

	public Mono<List<PokemonInfo>> getPokemonNextWebClientOffset(Long offset) {

		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v2/pokemon/").queryParam("offset", offset)
						.queryParam("limit", 250).build())
				.retrieve().bodyToMono(PokemonsGeneral.class)

				.flatMapMany(pokemonGeneral -> Flux.fromIterable(pokemonGeneral.getResults()))

				.flatMap(uri -> webClient.get().uri(uri.getUrl()).retrieve().bodyToMono(PokemonInfo.class))

				.collectList();
	}

	public Mono<List<PokemonWeight>> procesWeight() {

		PokemonsGeneral pokemonGeneral = webClient.get().uri(url).retrieve().bodyToMono(PokemonsGeneral.class).block();
		int numberOfCalls = Math.ceilDiv(pokemonGeneral.getCount(), 250);

		return Flux.range(0, numberOfCalls).flatMap(i -> findWeight((long) i * 250))
				.flatMapIterable(PokemonResponseWeight::getPokemonWeight)
				.sort(Comparator.comparingInt(PokemonWeight::getWeight).reversed()).take(5).collectList();
	}

	public Mono<List<PokemonHeight>> procesHeight() {

		PokemonsGeneral pokemonGeneral = webClient.get().uri(url).retrieve().bodyToMono(PokemonsGeneral.class).block();
		int numberOfCalls = Math.ceilDiv(pokemonGeneral.getCount(), 250);

		return Flux.range(0, numberOfCalls).flatMap(i -> findHeight((long) i * 250))
				.flatMapIterable(PokemonResponseHeight::getPokemonHeight)
				.sort(Comparator.comparingInt(PokemonHeight::getHeight).reversed()).take(5).collectList();
	}

	public Mono<List<PokemonExp>> procesExp() {

		PokemonsGeneral pokemonGeneral = webClient.get().uri(url).retrieve().bodyToMono(PokemonsGeneral.class).block();
		int numberOfCalls = Math.ceilDiv(pokemonGeneral.getCount(), 250);
		return Flux.range(0, numberOfCalls).flatMap(i -> findExp((long) i * 250))
				.flatMapIterable(PokemonResponseExp::getPokemonExp)
				.sort(Comparator.comparing(PokemonExp::getBase_experience, Comparator.reverseOrder())).take(5)
				.collectList();
	}

}
