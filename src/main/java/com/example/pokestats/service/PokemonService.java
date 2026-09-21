package com.example.pokestats.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	private RestTemplate restTemplate;

	@Autowired
	private WeightMapper weightMapper;
	@Autowired
	private HeightMapper heightMapper;
	@Autowired
	private ExpMapper expMapper;

	String url = "https://pokeapi.co/api/v2/pokemon/";

	public PokemonResponseWeight getPokemonWeightDefault() {

		PokemonsGeneral pokemonGeneral = restTemplate.getForObject(url, PokemonsGeneral.class);
		ArrayList<PokemonInfo> pokeInfo = new ArrayList<>();

		getPokemonNext(pokemonGeneral, pokeInfo);

		pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getWeight).reversed());
		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseWeight rsp = new PokemonResponseWeight();

		rsp.setPokemonInfo(weightMapper.toPokeWeight(list));
		return rsp;
	}

	public ArrayList<PokemonInfo> getPokemonGeneral() {

		PokemonsGeneral pokemonGeneral = restTemplate.getForObject(url, PokemonsGeneral.class);
		ArrayList<PokemonInfo> pokeInfo = new ArrayList<>();

		getPokemonNext(pokemonGeneral, pokeInfo);

		return pokeInfo;
	}

	public void getPokemonNext(PokemonsGeneral pokemonGeneral, ArrayList<PokemonInfo> pokeInfo) {

		for (PokemonUri uri : pokemonGeneral.getResults()) {
			pokeInfo.add(restTemplate.getForObject(uri.getUrl(), PokemonInfo.class));
		}

		if (pokemonGeneral.getNext() != null) {
			pokemonGeneral = restTemplate.getForObject(pokemonGeneral.getNext(), PokemonsGeneral.class);
			getPokemonNext(pokemonGeneral, pokeInfo);
		}

	}

	public PokemonResponseWeight getPokemonWeight() {

		ArrayList<PokemonInfo> pokeInfo = getPokemonGeneral();

		pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getWeight).reversed());
		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseWeight rsp = new PokemonResponseWeight();

		rsp.setPokemonInfo(weightMapper.toPokeWeight(list));
		return rsp;
	}

	public PokemonResponseHeight getPokemonHeight() {

		ArrayList<PokemonInfo> pokeInfo = getPokemonGeneral();

		pokeInfo.sort(Comparator.comparingInt(PokemonInfo::getHeight).reversed());
		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseHeight rsp = new PokemonResponseHeight();

		rsp.setPokemonInfo(heightMapper.toPokeHeight(list));
		return rsp;
	}

	public PokemonResponseExp getPokemonExp() {

		ArrayList<PokemonInfo> pokeInfo = getPokemonGeneral();
		pokeInfo.removeIf(o -> o.getBase_experience() == null);
		pokeInfo.sort(Comparator.comparingLong(PokemonInfo::getBase_experience).reversed());

		List<PokemonInfo> list = pokeInfo.subList(0, 5);
		PokemonResponseExp rsp = new PokemonResponseExp();

		rsp.setPokemonInfo(expMapper.toPokeExp(list));
		return rsp;
	}
}
