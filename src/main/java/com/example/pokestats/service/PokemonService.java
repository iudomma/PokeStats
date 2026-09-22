package com.example.pokestats.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class PokemonService {

	@Autowired
	private WebClient webClient;
	@Autowired
	private WeightMapper weightMapper;
	@Autowired
	private HeightMapper heightMapper;
	@Autowired
	private ExpMapper expMapper;

	String url = "https://pokeapi.co/api/v2/pokemon/";

	public ArrayList<PokemonInfo> getPokemonGeneralWebClient() {

		PokemonsGeneral pokemonGeneral = webClient.get().uri(url).retrieve().bodyToMono(PokemonsGeneral.class).block();
		ArrayList<PokemonInfo> pokeInfo = new ArrayList<>();

		getPokemonNextWebClient(pokemonGeneral, pokeInfo);

		return pokeInfo;
	}

	public void getPokemonNextWebClient(PokemonsGeneral pokemonGeneral, ArrayList<PokemonInfo> pokeInfo) {

		for (PokemonUri uri : pokemonGeneral.getResults()) {
			pokeInfo.add(webClient.get().uri(uri.getUrl()).retrieve().bodyToMono(PokemonInfo.class).block());

		}

		if (pokemonGeneral.getNext() != null) {
			pokemonGeneral = webClient.get().uri(pokemonGeneral.getNext()).retrieve().bodyToMono(PokemonsGeneral.class)
					.block();
			getPokemonNextWebClient(pokemonGeneral, pokeInfo);
		}

	}

	public PokemonResponseWeight getPokemonWeight() {

		ArrayList<PokemonInfo> pokeInfo = getPokemonGeneralWebClient();

		pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getWeight).reversed());
		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseWeight rsp = new PokemonResponseWeight();

		rsp.setPokemonInfo(weightMapper.toPokeWeight(list));
		return rsp;
	}

	public PokemonResponseHeight getPokemonHeight() {

		ArrayList<PokemonInfo> pokeInfo = getPokemonGeneralWebClient();

		pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getHeight).reversed());
		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseHeight rsp = new PokemonResponseHeight();

		rsp.setPokemonHeight(heightMapper.toPokeHeight(list));
		return rsp;
	}

	public PokemonResponseExp getPokemonExp() {

		ArrayList<PokemonInfo> pokeInfo = getPokemonGeneralWebClient();
		pokeInfo.removeIf(o -> o.getBase_experience() == null);
		pokeInfo.sort(Comparator.comparingLong(PokemonInfo::getBase_experience).reversed());

		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseExp rsp = new PokemonResponseExp();

		rsp.setPokemonExp(expMapper.toPokeExp(list));
		return rsp;
	}

}
