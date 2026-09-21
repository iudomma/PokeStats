package com.example.pokestats.dto;

import java.util.List;

public class PokemonResponseWeight {

	private List<PokemonWeight> pokeWeight;

	public List<PokemonWeight> getPokemonInfo() {
		return pokeWeight;
	}

	public void setPokemonInfo(List<PokemonWeight> pokeWeight) {
		this.pokeWeight = pokeWeight;
	}

}
